package br.com.abimael.cursotestes.model;

import br.com.abimael.cursotestes.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTask {
  private Long taskId;
  private TaskStatus status;
}
