package com.tecnova.technical_test.infrastructure.adapter.repository;

import com.tecnova.technical_test.infrastructure.adapter.entity.TaskStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskStatusRepository extends JpaRepository<TaskStatusEntity, Long> {
}
