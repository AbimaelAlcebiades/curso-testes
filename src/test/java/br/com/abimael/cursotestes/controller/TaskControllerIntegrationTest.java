package br.com.abimael.cursotestes.controller;

import br.com.abimael.cursotestes.utils.AbstractTestContainers;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("IntegrationTest")
@SpringBootTest
class TaskControllerIntegrationTest extends AbstractTestContainers {


  @BeforeEach
  void setUp() {
    cleanEnvironment();
  }

  @SneakyThrows
  @Test
  @DisplayName("WHEN POST valid TaskJson THEN should insert task on database")
  void testPostValidTaskJson() {
    //SETUP
    //PROCESSING
    //ASSERT
  }

}
