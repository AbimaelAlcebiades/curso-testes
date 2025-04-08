package br.com.abimael.cursotestes.services;

import br.com.abimael.cursotestes.exception.CreateTaskException;
import br.com.abimael.cursotestes.exception.TaskNotFoundException;
import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  public TaskJson insert(final CreateTask createTask) throws CreateTaskException {
    return null;
  }

  public TaskJson getTask(Long taskId) throws TaskNotFoundException {
    return null;
  }
}
