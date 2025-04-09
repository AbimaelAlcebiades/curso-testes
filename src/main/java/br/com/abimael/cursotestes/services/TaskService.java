package br.com.abimael.cursotestes.services;

import br.com.abimael.cursotestes.entity.TaskEntity;
import br.com.abimael.cursotestes.event.TaskJsonSavedEvent;
import br.com.abimael.cursotestes.exception.ServiceException;
import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import br.com.abimael.cursotestes.model.UpdateTask;
import br.com.abimael.cursotestes.model.converter.TaskEntityConverter;
import br.com.abimael.cursotestes.model.converter.TaskJsonConverter;
import br.com.abimael.cursotestes.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

  private TaskEntityConverter taskEntityConverter;
  private TaskRepository taskRepository;
  private TaskJsonConverter taskJsonConverter;
  private ApplicationEventPublisher applicationEventPublisher;

  public TaskJson insert(CreateTask createTask) throws ServiceException {
    try {
      return taskJsonConverter.from(taskRepository.save(taskEntityConverter.from(createTask)));
    } catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }
  }

  public TaskJson getTask(Long taskId) throws ServiceException {
    try {
      return taskRepository
          .findById(taskId)
          .map(taskEntity -> taskJsonConverter.from(taskEntity))
          .orElse(null);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }
  }

  public TaskJson update(UpdateTask updateTask) throws ServiceException {
    try {
      return taskRepository
          .findById(updateTask.getTaskId())
          .map(
              taskEntity -> {
                taskEntity.setTaskStatus(updateTask.getStatus());
                return taskRepository.save(taskEntity);
              })
          .map(
              taskEntity -> {
                TaskJson taskJsonSaved = taskJsonConverter.from(taskEntity);
                applicationEventPublisher.publishEvent(new TaskJsonSavedEvent(this, taskJsonSaved));
                return taskJsonSaved;
              })
          .orElse(null);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }
  }
}
