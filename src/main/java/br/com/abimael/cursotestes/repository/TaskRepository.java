package br.com.abimael.cursotestes.repository;

import br.com.abimael.cursotestes.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {}
