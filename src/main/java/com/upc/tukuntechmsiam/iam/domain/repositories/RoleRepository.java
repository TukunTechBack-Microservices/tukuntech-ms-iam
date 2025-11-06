package com.upc.tukuntechmsiam.iam.domain.repositories;

import com.upc.tukuntechmsiam.iam.domain.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(String name);
}
