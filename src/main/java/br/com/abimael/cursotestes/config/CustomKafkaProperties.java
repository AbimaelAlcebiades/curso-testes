package br.com.abimael.cursotestes.config;

import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@EqualsAndHashCode(callSuper = true)
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
@Data
@Primary
public class CustomKafkaProperties extends KafkaProperties {
  private Integer healthCheckTimeoutSeconds;
  private Map<String, String> topics;
}
