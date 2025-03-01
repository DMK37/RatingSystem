package org.example.ratingsystem.repositories;

import io.lettuce.core.dynamic.annotation.Param;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
