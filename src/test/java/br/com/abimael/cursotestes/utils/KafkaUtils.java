package br.com.abimael.cursotestes.utils;

import static br.com.abimael.cursotestes.utils.AbstractTestContainers.kafkaConsumerProperties;
import static java.time.Duration.*;
import static java.util.Collections.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static List<JsonNode> getJsonFromTopic(String topic) {
    return getMessagesFromTopic(topic).stream()
        .map(
            jsonMessage -> {
              try {
                return mapper.readTree(jsonMessage);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            })
        .toList();
  }

  public static List<String> getMessagesFromTopic(String topic) {
    List<String> messages = new ArrayList<>();

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kafkaConsumerProperties)) {
      consumer.subscribe(singletonList(topic));

      ConsumerRecords<String, String> records = ConsumerRecords.empty();
      long timeout = System.currentTimeMillis() + 5000;

      while (records.isEmpty() && System.currentTimeMillis() < timeout) {
        records = consumer.poll(ofMillis(500));
      }

      for (ConsumerRecord<String, String> consumerRecord : records) {
        messages.add(consumerRecord.value());
      }
    }

    return messages;
  }
}
