package br.com.abimael.cursotestes.utils.builders;

import static br.com.abimael.cursotestes.enums.TaskType.PLAYBACK;

import br.com.abimael.cursotestes.enums.TaskType;
import br.com.abimael.cursotestes.model.CreateTask;

public class CreateTaskBuilder {
  public static CreateTask VALID_CREATE_TASK() {
    return CreateTask.builder().deviceId("DEVICE_ID").taskType(PLAYBACK).build();
  }

  public static CreateTask EMPTY_CREATE_TASK() {
    return CreateTask.builder().build();
  }

  public static CreateTask CREATE_TASK_WITH(String deviceId, TaskType taskType) {
    return CreateTask.builder().deviceId(deviceId).taskType(taskType).build();
  }
}
