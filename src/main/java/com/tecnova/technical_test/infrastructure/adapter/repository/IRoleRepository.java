package com.tecnova.technical_test.infrastructure.adapter.repository;

import com.tecnova.technical_test.infrastructure.adapter.entity.RoleEntity;
import com.tecnova.technical_test.infrastructure.adapter.enums.RoleList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Integer> {

    Optional<RoleEntity> findByName(RoleList name);
}
