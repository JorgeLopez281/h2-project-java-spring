package com.tecnova.technical_test.infrastructure.adapter.repository;

import com.tecnova.technical_test.infrastructure.adapter.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
}
