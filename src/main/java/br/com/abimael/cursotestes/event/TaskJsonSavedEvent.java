package br.com.abimael.cursotestes.event;

import br.com.abimael.cursotestes.model.TaskJson;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskJsonSavedEvent extends ApplicationEvent {

  private final transient TaskJson taskJsonSaved;

  public TaskJsonSavedEvent(Object source, TaskJson taskJsonSaved) {
    super(source);
    this.taskJsonSaved = taskJsonSaved;
  }
}
