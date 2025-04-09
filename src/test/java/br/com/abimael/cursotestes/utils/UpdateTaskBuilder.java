package br.com.abimael.cursotestes.utils;

import br.com.abimael.cursotestes.enums.TaskStatus;
import br.com.abimael.cursotestes.model.UpdateTask;

public class UpdateTaskBuilder {
  public static UpdateTask UPDATE_TASK_WITH(long taskId, TaskStatus taskStatus) {
    return UpdateTask.builder().taskId(taskId).status(taskStatus).build();
  }
}
