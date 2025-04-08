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
    return taskService.insert(createTask);
  }

  public TaskJson retrieve(final Long taskId) throws ServiceException {
    return taskService.getTask(taskId);
  }
}
