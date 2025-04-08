package br.com.abimael.cursotestes.services;

import br.com.abimael.cursotestes.entity.TaskEntity;
import br.com.abimael.cursotestes.exception.ServiceException;
import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import br.com.abimael.cursotestes.model.converter.TaskEntityConverter;
import br.com.abimael.cursotestes.model.converter.TaskJsonConverter;
import br.com.abimael.cursotestes.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskService {

  private TaskEntityConverter taskEntityConverter;
  private TaskRepository taskRepository;
  private TaskJsonConverter taskJsonConverter;

  public TaskJson insert(CreateTask createTask) throws ServiceException {
    try {
      TaskEntity saved = taskRepository.save(taskEntityConverter.from(createTask));
      return taskJsonConverter.from(saved);
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
}
