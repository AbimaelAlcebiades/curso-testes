package br.com.abimael.cursotestes.exception;

public class TaskNotFoundException extends Exception {
  public TaskNotFoundException(final String message) {
    super(message);
  }
}
