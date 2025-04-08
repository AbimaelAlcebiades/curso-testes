package br.com.abimael.cursotestes.utils.builders;

import br.com.abimael.cursotestes.model.CreateTask;

import static br.com.abimael.cursotestes.enums.TaskType.PLAYBACK;

public class CreateTaskBuilder {
  public static CreateTask VALID_CREATE_TASK() {
    return CreateTask.builder().deviceId("DEVICE_ID").taskType(PLAYBACK).build();
  }

  public static CreateTask EMPTY_CREATE_TASK() {
    return CreateTask.builder().build();
  }
}
