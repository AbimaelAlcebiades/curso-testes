package br.com.abimael.cursotestes.builders;

import br.com.abimael.cursotestes.model.TaskJson;

public class TaskJsonBuilder {
  public static TaskJson VALID_TASK_JSON() {
    return TaskJson.builder().build();
  }
}
