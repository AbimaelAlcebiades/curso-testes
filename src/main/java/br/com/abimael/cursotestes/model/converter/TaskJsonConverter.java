package br.com.abimael.cursotestes.model.converter;

import br.com.abimael.cursotestes.entity.TaskEntity;
import br.com.abimael.cursotestes.model.TaskJson;
import org.springframework.stereotype.Component;

@Component
public class TaskJsonConverter {
  public TaskJson from(TaskEntity taskEntity) {
    return TaskJson.builder()
        .id(taskEntity.getId())
        .deviceId(taskEntity.getDeviceId())
        .type(taskEntity.getTaskType())
        .status(taskEntity.getTaskStatus())
        .createdAt(taskEntity.getCreatedAt())
        .build();
  }
}
