package br.com.abimael.cursotestes.controller;

import br.com.abimael.cursotestes.exception.ServiceException;
import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import br.com.abimael.cursotestes.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class TaskController {

  private final TaskService taskService;

  public TaskJson insert(CreateTask createTask) throws ServiceException {
    try {
      return taskService.insert(createTask);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }
  }

  public TaskJson retrieve(final Long taskId) throws ServiceException {
    try {
      return taskService.getTask(taskId);
    } catch (Exception e) {
      throw new ServiceException(e.getMessage());
    }
  }
}
