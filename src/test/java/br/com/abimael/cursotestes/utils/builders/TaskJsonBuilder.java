package br.com.abimael.cursotestes.utils.builders;

import br.com.abimael.cursotestes.enums.TaskStatus;
import br.com.abimael.cursotestes.enums.TaskType;
import br.com.abimael.cursotestes.model.TaskJson;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskJsonBuilder {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static TaskJson VALID_TASK_JSON() {
    return TaskJson.builder().build();
  }

  public static TaskJson TASK_JSON_WITH(
      Long taskId, String deviceId, TaskType taskType, TaskStatus taskStatus) {
    return TaskJson.builder()
        .id(taskId)
        .deviceId(deviceId)
        .type(taskType)
        .status(taskStatus)
        .build();
  }
}
