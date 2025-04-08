package br.com.abimael.cursotestes.model;

import br.com.abimael.cursotestes.enums.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTask {
  private String deviceId;
  private TaskType taskType;
}
