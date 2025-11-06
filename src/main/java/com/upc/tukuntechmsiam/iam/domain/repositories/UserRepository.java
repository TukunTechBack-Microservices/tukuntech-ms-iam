package com.upc.tukuntechmsiam.iam.domain.repositories;

import com.upc.tukuntechmsiam.iam.domain.entity.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserIdentity, Long> {
    Optional<UserIdentity> findByEmail(String email);
}
