package br.com.abimael.cursotestes.services;

import static br.com.abimael.cursotestes.utils.builders.CreateTaskBuilder.*;
import static br.com.abimael.cursotestes.utils.builders.TaskEntityBuilder.*;
import static br.com.abimael.cursotestes.utils.builders.TaskJsonBuilder.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.com.abimael.cursotestes.entity.TaskEntity;
import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import br.com.abimael.cursotestes.model.converter.TaskEntityConverter;
import br.com.abimael.cursotestes.model.converter.TaskJsonConverter;
import br.com.abimael.cursotestes.repository.TaskRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {

  @Mock private TaskEntityConverter taskEntityConverter;
  @Mock private TaskRepository taskRepository;
  @Mock private TaskJsonConverter taskJsonConverter;

  @InjectMocks private TaskService taskService;

  @Test
  @SneakyThrows
  @DisplayName("WHEN insert valid task THEN should save using TaskService")
  void insertValidTask() {
    // SETUP
    CreateTask createTask = VALID_CREATE_TASK();
    TaskEntity taskEntity = VALID_TASK_ENTITY();
    TaskJson taskJsonSaved = VALID_TASK_JSON();

    when(taskEntityConverter.from(createTask)).thenReturn(taskEntity);
    when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
    when(taskJsonConverter.from(taskEntity)).thenReturn(taskJsonSaved);

    // PROCESSING
    TaskJson taskJson = taskService.insert(createTask);

    // ASSERT
    assertNotNull(taskJson);
  }
}
