package br.com.abimael.cursotestes.services;

import br.com.abimael.cursotestes.exception.ServiceException;
import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  public TaskJson insert(final CreateTask createTask) throws ServiceException {
    return null;
  }

  public TaskJson getTask(Long taskId) throws ServiceException {
    return null;
  }

  public List<TaskJson> getTasks() throws ServiceException {
    return null;
  }
}
