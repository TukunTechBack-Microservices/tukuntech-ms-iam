package com.upc.tukuntechmsiam.iam.domain.repositories;

import com.upc.tukuntechmsiam.iam.domain.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByName(String name);
}
