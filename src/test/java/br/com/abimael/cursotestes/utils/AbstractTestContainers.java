package br.com.abimael.cursotestes.utils;

import static br.com.abimael.cursotestes.utils.DatabaseUtils.cleanDataBase;
import static br.com.abimael.cursotestes.utils.DatabaseUtils.setConnectionConfigs;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;
import static org.testcontainers.utility.DockerImageName.*;
import static org.testcontainers.utility.DockerImageName.parse;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.ClassRule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@ContextConfiguration(
    initializers = AbstractTestContainers.DockerPostgresDataSourceInitializer.class)
@Testcontainers
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integrationtests")
@Slf4j
@AutoConfigureMockMvc
public abstract class AbstractTestContainers {

  @ClassRule public static PostgreSQLContainer<?> postgresDBContainer;

  @ClassRule public static KafkaContainer kafkaContainer;

  public static Properties kafkaConsumerProperties;

  static {
    startPostgresContainer();
    startKafkaContainer();
  }

  private static void startPostgresContainer() {
    var config = new YamlConfigLoader("application-integrationtests.yml");
    String username = (String) config.getNestedConfigValue("spring", "datasource", "username");
    String password = (String) config.getNestedConfigValue("spring", "datasource", "password");

    postgresDBContainer =
        new PostgreSQLContainer<>("postgres:13-alpine")
            .withExposedPorts(5432)
            .withUsername(username)
            .withPassword(password)
            .withDatabaseName("cursotestes")
            .withLogConsumer(new Slf4jLogConsumer(getLogger(AbstractTestContainers.class)));

    postgresDBContainer.start();
  }

  private static void startKafkaContainer() {
    kafkaContainer =
        new KafkaContainer(parse("confluentinc/cp-kafka:7.2.1"))
            .withEmbeddedZookeeper()
            .withExposedPorts(9093);

    kafkaContainer.start();
  }

  public static class DockerPostgresDataSourceInitializer
      implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
          context,
          "spring.datasource.user=" + postgresDBContainer.getUsername(),
          "spring.datasource.password=" + postgresDBContainer.getPassword(),
          "spring.datasource.url=" + postgresDBContainer.getJdbcUrl(),
          "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers());

      logContainerConfigs();
      kafkaConsumerProperties = buildKafkaConsumerProperties();
    }

    private void logContainerConfigs() {
      log.info("spring.datasource.user={}", postgresDBContainer.getUsername());
      log.info("spring.datasource.password={}", postgresDBContainer.getPassword());
      log.info("spring.datasource.url={}", postgresDBContainer.getJdbcUrl());
      log.info("spring.kafka.bootstrap-servers={}", kafkaContainer.getBootstrapServers());
    }
  }

  public static void cleanEnvironment() {
    setConnectionConfigs(
        postgresDBContainer.getJdbcUrl(),
        postgresDBContainer.getUsername(),
        postgresDBContainer.getPassword());
    cleanDataBase();
  }

  private static Properties buildKafkaConsumerProperties() {
    Properties props = new Properties();
    props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
    props.put(GROUP_ID_CONFIG, "curso-testes-integration-tests");
    props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(TRUSTED_PACKAGES, "br.com.crearesistemas.*");
    return props;
  }
}
