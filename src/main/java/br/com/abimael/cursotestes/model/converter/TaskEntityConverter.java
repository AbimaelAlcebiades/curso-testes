package br.com.abimael.cursotestes.model.converter;

import static br.com.abimael.cursotestes.enums.TaskStatus.*;
import static java.time.Instant.*;

import br.com.abimael.cursotestes.entity.TaskEntity;
import br.com.abimael.cursotestes.model.CreateTask;
import org.springframework.stereotype.Component;

@Component
public class TaskEntityConverter {
  public TaskEntity from(CreateTask createTask) {
    return TaskEntity.builder()
        .taskType(createTask.getTaskType())
        .taskStatus(CREATED)
        .deviceId(createTask.getDeviceId())
        .createdAt(now())
        .build();
  }
}
