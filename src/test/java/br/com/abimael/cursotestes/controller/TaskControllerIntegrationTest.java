package br.com.abimael.cursotestes.controller;

import static br.com.abimael.cursotestes.enums.TaskStatus.*;
import static br.com.abimael.cursotestes.enums.TaskType.PLAYBACK;
import static br.com.abimael.cursotestes.enums.TaskType.SNAPSHOT;
import static br.com.abimael.cursotestes.utils.builders.CreateTaskBuilder.*;
import static br.com.abimael.cursotestes.utils.builders.TaskJsonBuilder.*;
import static java.time.Instant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.com.github.dockerjava.core.MediaType.APPLICATION_JSON;

import br.com.abimael.cursotestes.model.CreateTask;
import br.com.abimael.cursotestes.model.TaskJson;
import br.com.abimael.cursotestes.utils.AbstractTestContainers;
import br.com.abimael.cursotestes.utils.mock.TasksMockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@Tag("IntegrationTest")
@SpringBootTest
class TaskControllerIntegrationTest extends AbstractTestContainers {

  @Autowired private TasksMockMvc tasksMockMvc;

  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

  @BeforeEach
  void setUp() {
    cleanEnvironment();
  }

  @SneakyThrows
  @Test
  @DisplayName("WHEN POST valid TaskJson THEN should insert task on database")
  void testPostValidTaskJson() {
    // SETUP
    CreateTask createTask = VALID_CREATE_TASK();

    // PROCESSING
    MvcResult mvcResult =
        tasksMockMvc.performPostJson("/task", mapper.writeValueAsString(createTask)).andReturn();

    // ASSERT
    MockHttpServletResponse response = mvcResult.getResponse();
    int status = response.getStatus();
    String contentType = response.getContentType();
    String content = response.getContentAsString();
    TaskJson expectedTaskJson = TASK_JSON_WITH(1L, "DEVICE_ID", PLAYBACK, CREATED);
    TaskJson curretnTaskJson = mapper.readValue(content, TaskJson.class);

    assertEquals(HttpStatus.CREATED.value(), status);
    assertNotNull(content);
    assertEquals(APPLICATION_JSON.getMediaType(), contentType);
    assertEquals(expectedTaskJson.getId(), curretnTaskJson.getId());
    assertEquals(expectedTaskJson.getDeviceId(), curretnTaskJson.getDeviceId());
    assertEquals(expectedTaskJson.getStatus(), curretnTaskJson.getStatus());
    assertEquals(expectedTaskJson.getType(), curretnTaskJson.getType());
    assertNotNull(curretnTaskJson.getCreatedAt());
    assertTrue(curretnTaskJson.getCreatedAt().isBefore(now()));
  }

  @SneakyThrows
  @Test
  @DisplayName("WHEN GET task by id from endpoint /task THEN should return task from database")
  void testGetTaskJson() {
    // SETUP
    CreateTask createTaskPlayback = CREATE_TASK_WITH("DEVICE1", PLAYBACK);
    CreateTask createTaskSnapshot = CREATE_TASK_WITH("DEVICE2", SNAPSHOT);

    // PROCESSING
    tasksMockMvc
        .performPostJson("/task", mapper.writeValueAsString(createTaskPlayback))
        .andReturn();
    tasksMockMvc
        .performPostJson("/task", mapper.writeValueAsString(createTaskSnapshot))
        .andReturn();
    MvcResult mvcResultTaskPlayback =
        tasksMockMvc.performGetWithQueryParameters("/task", Map.of("taskId", 1L)).andReturn();
    MvcResult mvcResultTaskSnapshot =
        tasksMockMvc.performGetWithQueryParameters("/task", Map.of("taskId", 2L)).andReturn();

    // ASSERT
    MockHttpServletResponse responseTaskPlayback = mvcResultTaskPlayback.getResponse();
    TaskJson taskJsonPlayback =
        mapper.readValue(responseTaskPlayback.getContentAsString(), TaskJson.class);
    MockHttpServletResponse responseTaskSnapshot = mvcResultTaskSnapshot.getResponse();
    TaskJson taskJsonSnapshot =
        mapper.readValue(responseTaskSnapshot.getContentAsString(), TaskJson.class);

    assertEquals(HttpStatus.OK.value(), responseTaskPlayback.getStatus());
    assertNotNull(responseTaskPlayback.getContentType());
    assertEquals(APPLICATION_JSON.getMediaType(), responseTaskPlayback.getContentType());

    assertEquals(1L, taskJsonPlayback.getId());
    assertEquals(PLAYBACK, taskJsonPlayback.getType());
    assertEquals("DEVICE1", taskJsonPlayback.getDeviceId());
    assertEquals(2L, taskJsonSnapshot.getId());
    assertEquals(SNAPSHOT, taskJsonSnapshot.getType());
    assertEquals("DEVICE2", taskJsonSnapshot.getDeviceId());
  }
}
