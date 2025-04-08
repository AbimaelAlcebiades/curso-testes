package br.com.abimael.cursotestes;

import br.com.abimael.cursotestes.controller.TaskController;
import br.com.abimael.cursotestes.exception.CreateTaskException;
import br.com.abimael.cursotestes.exception.ServiceException;
import br.com.abimael.cursotestes.exception.TaskNotFoundException;
import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import br.com.abimael.cursotestes.services.TaskService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static br.com.abimael.cursotestes.builders.CreateTaskBuilder.EMPTY_CREATE_TASK;
import static br.com.abimael.cursotestes.builders.CreateTaskBuilder.VALID_CREATE_TASK;
import static br.com.abimael.cursotestes.builders.TaskJsonBuilder.VALID_TASK_JSON;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TaskControllerTest {

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  @Test
  @SneakyThrows
  @DisplayName("WHEN insert valid task THEN should save using TaskService")
  void insertValidTask() {
    //SETUP
    CreateTask validCreateTask = VALID_CREATE_TASK();

    when(taskService.insert(validCreateTask)).thenReturn(VALID_TASK_JSON());

    //PROCESSING
    TaskJson createdTaskJson = taskController.insert(validCreateTask);

    //ASSERTS
    assertNotNull(createdTaskJson);
  }

  @Test
  @SneakyThrows
  @DisplayName("WHEN taskService throws Exception THEN should throws ServiceException")
  void insertInvalidTask() {
    //SETUP
    CreateTask emptyCreateTask = EMPTY_CREATE_TASK();

    when(taskService.insert(emptyCreateTask)).thenThrow(new CreateTaskException("ERROR"));

    //PROCESSING
    ServiceException serviceException = assertThrows(ServiceException.class, () -> taskController.insert(emptyCreateTask));

    //ASSERTS
    assertEquals("ERROR", serviceException.getMessage());
  }

  @Test
  @SneakyThrows
  @DisplayName("WHEN search existing task THEN should return task using TaskService")
  void retrieveTask() {
    //SETUP
    Long taskId = 1L;

    when(taskService.getTask(taskId)).thenReturn(VALID_TASK_JSON());

    //PROCESSING
    TaskJson taskJsonFound = taskController.retrieve(taskId);

    //ASSERTS
    assertNotNull(taskJsonFound);
  }

  @Test
  @SneakyThrows
  @DisplayName("WHEN search not exist task THEN should throws TaskNotFoundException")
  void notFoundTask() {
    //SETUP
    Long taskIdNotExist = 1L;
    when(taskService.getTask(taskIdNotExist)).thenThrow(new TaskNotFoundException("NOT FOUND TASK"));

    //PROCESSING
    ServiceException serviceException = assertThrows(ServiceException.class, () -> taskController.retrieve(taskIdNotExist));

    //ASSERTS
    assertEquals("NOT FOUND TASK", serviceException.getMessage());
  }

}
