package org.example.ratingsystem.repositories;

import org.example.ratingsystem.entities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {
}
