package br.com.abimael.cursotestes.model;

import br.com.abimael.cursotestes.enums.TaskStatus;
import br.com.abimael.cursotestes.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskJson {
  private Long id;
  private String deviceId;
  private TaskType type;
  private Instant createdAt;
  private TaskStatus status;
}
