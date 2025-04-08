package br.com.abimael.cursotestes.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.abimael.cursotestes.exception.ServiceException;
import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import br.com.abimael.cursotestes.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class TaskController {

  private final TaskService taskService;

  @PostMapping(value = "/task", produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(CREATED)
  public TaskJson insert(@RequestBody CreateTask createTask) throws ServiceException {
    return taskService.insert(createTask);
  }

  @GetMapping(value = "/task", produces = APPLICATION_JSON_VALUE)
  public TaskJson getTaskById(@RequestParam("taskId") Long taskId) throws ServiceException {
    return taskService.getTask(taskId);
  }
}
