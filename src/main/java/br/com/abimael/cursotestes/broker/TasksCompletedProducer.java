package br.com.abimael.cursotestes.broker;

import br.com.abimael.cursotestes.config.CustomKafkaProperties;
import br.com.abimael.cursotestes.entity.TaskEntity;
import br.com.abimael.cursotestes.model.TaskJson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TasksCompletedProducer {

  private KafkaTemplate<String, TaskJson> kafkaTemplate;
  private CustomKafkaProperties customKafkaProperties;

  public void produce(TaskJson taskJsonSaved) {
    String topicName = customKafkaProperties.getTopics().get("tasks-completed");
    try {
      log.debug("Produce message in topic {} | {}", topicName, taskJsonSaved);
      kafkaTemplate.send(topicName, taskJsonSaved);
    } catch (Exception e) {
      log.error(
          "Error on produce message {} in topic {}, message was ignored. {}",
          taskJsonSaved,
          topicName,
          e.getMessage());
    }
  }
}
