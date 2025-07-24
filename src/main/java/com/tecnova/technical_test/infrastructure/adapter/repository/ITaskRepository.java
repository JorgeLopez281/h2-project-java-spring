package com.tecnova.technical_test.infrastructure.adapter.repository;

import com.tecnova.technical_test.infrastructure.adapter.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskRepository extends JpaRepository<TaskEntity, Long> {
}
