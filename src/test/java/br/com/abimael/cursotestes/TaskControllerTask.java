package br.com.abimael.cursotestes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TaskControllerTask {

  @Test
  @DisplayName("WHEN insert valid task THEN should save using repository")
  void insertValidTask() {
    //SETUP
    //PROCESSING
    //ASSERTS
  }

  @Test
  @DisplayName("WHEN insert invalid task THEN should throws InvalidTaskException")
  void insertInvalidTask() {
    //SETUP
    //PROCESSING
    //ASSERTS
  }

  @Test
  @DisplayName("WHEN search existing task THEN should return task using repository")
  void retrieveTask() {
    //SETUP
    //PROCESSING
    //ASSERTS
  }

  @Test
  @DisplayName("WHEN search not exist task THEN should throws TaskNotFoundException")
  void notFoundTask() {
    //SETUP
    //PROCESSING
    //ASSERTS
  }

}
