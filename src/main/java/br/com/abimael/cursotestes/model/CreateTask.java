package br.com.abimael.cursotestes.model;

import br.com.abimael.cursotestes.enums.TaskType;
import lombok.Builder;

@Builder
public class CreateTask {
  private String deviceId;
  private TaskType taskType;
}
