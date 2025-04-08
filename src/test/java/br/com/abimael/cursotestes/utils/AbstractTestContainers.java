package br.com.abimael.cursotestes.utils;

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

import java.util.Properties;

import static br.com.abimael.cursotestes.utils.AbstractTestContainers.DockerPostgresDataSourceInitializer;
import static br.com.abimael.cursotestes.utils.SqlScriptMerger.mergeAllSqlScripts;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;
import static org.testcontainers.utility.DockerImageName.parse;

@SpringBootTest(webEnvironment = DEFINED_PORT)
@ContextConfiguration(
        initializers = DockerPostgresDataSourceInitializer.class)
@Testcontainers
@ExtendWith(SpringExtension.class)
@ActiveProfiles("integrationtests")
@Slf4j
@AutoConfigureMockMvc
public abstract class AbstractTestContainers {

  @ClassRule
  public static PostgreSQLContainer<?> postgresDBContainer;

  @ClassRule
  public static KafkaContainer kafkaContainer;

  public static Properties kafkaConsumerProperties;

  static {
    YamlConfigLoader configLoader = new YamlConfigLoader("application-integrationtests.yml");

    String username =
            (String) configLoader.getNestedConfigValue("spring", "datasource", "username");
    String password =
            (String) configLoader.getNestedConfigValue("spring", "datasource", "password");
    int exposedPorts = 5432;
    String dataBase = "jimi";

    Slf4jLogConsumer logger = new Slf4jLogConsumer(getLogger(AbstractTestContainers.class));

    postgresDBContainer =
            new PostgreSQLContainer<>(parse("postgres:13-alpine"))
                    .withExposedPorts(exposedPorts)
                    .withUsername(username)
                    .withPassword(password)
                    .withDatabaseName(dataBase)
                    .withLogConsumer(logger)
                    .withInitScript(mergeAllSqlScripts("src/test/resources", "merged_init.sql"));

    kafkaContainer =
            new KafkaContainer(parse("confluentinc/cp-kafka:7.2.1"))
                    .withEmbeddedZookeeper()
                    .withExposedPorts(9093);

    postgresDBContainer.start();
    kafkaContainer.start();
  }

  public static class DockerPostgresDataSourceInitializer
          implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
              applicationContext,
              "spring.datasource.user=" + postgresDBContainer.getUsername(),
              "spring.datasource.password=" + postgresDBContainer.getPassword(),
              "spring.datasource.url=" + postgresDBContainer.getJdbcUrl(),
              "spring.kafka.bootstrap-servers=" + kafkaContainer.getBootstrapServers());

      /*log.info("spring.datasource.user={}", postgresDBContainer.getUsername());
      log.info("spring.datasource.password={}", postgresDBContainer.getPassword());
      log.info("spring.datasource.url={}", postgresDBContainer.getJdbcUrl());
      log.info("spring.kafka.bootstrap-servers={}", kafkaContainer.getBootstrapServers());*/

      kafkaConsumerProperties = buildKafkaConsumerProperties(kafkaContainer);

    }
  }

  public static Properties buildKafkaConsumerProperties(KafkaContainer kafkaContainer) {
    Properties consumerProps = new Properties();
    consumerProps.put(BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
    consumerProps.put(GROUP_ID_CONFIG, "jimi-listener-v6-integration-tests");
    consumerProps.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    consumerProps.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    consumerProps.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
    consumerProps.put(TRUSTED_PACKAGES, "br.com.crearesistemas.*");
    return consumerProps;
  }
}
