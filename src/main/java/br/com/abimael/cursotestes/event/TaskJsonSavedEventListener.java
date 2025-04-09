package br.com.abimael.cursotestes.event;

import static br.com.abimael.cursotestes.enums.TaskStatus.COMPLETED;

import br.com.abimael.cursotestes.broker.TasksCompletedProducer;
import br.com.abimael.cursotestes.model.TaskJson;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class TaskJsonSavedEventListener implements ApplicationListener<TaskJsonSavedEvent> {

  private TasksCompletedProducer tasksCompletedProducer;

  @Override
  public void onApplicationEvent(@NonNull TaskJsonSavedEvent taskJsonSavedEvent) {
    try {
      TaskJson taskJsonSaved = taskJsonSavedEvent.getTaskJsonSaved();
      if (taskJsonSaved.getStatus() == COMPLETED) {
        tasksCompletedProducer.produce(taskJsonSavedEvent.getTaskJsonSaved());
      }
    } catch (Exception e) {
      log.error(
          "Error on process task json saved event {}, message was ignored and won't send again. {}",
          taskJsonSavedEvent,
          e.getMessage());
    }
  }
}
