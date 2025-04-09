package br.com.abimael.cursotestes.entity;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;

import br.com.abimael.cursotestes.enums.TaskStatus;
import br.com.abimael.cursotestes.enums.TaskType;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.*;

@Entity
@Table(name = "tasks", schema = "cursotestes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "task_type_id")
  @Enumerated(STRING)
  private TaskType taskType;

  @Column(name = "device_id", length = 30)
  private String deviceId;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "task_status_id")
  @Enumerated(STRING)
  private TaskStatus taskStatus;
}
