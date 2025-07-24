package com.tecnova.technical_test.infrastructure.adapter.repository;

import com.tecnova.technical_test.infrastructure.adapter.entity.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAppUserRepository extends JpaRepository<AppUserEntity, String> {

    Optional<AppUserEntity> findByUserName(String username);
    boolean existsByUserName(String userName);
}
