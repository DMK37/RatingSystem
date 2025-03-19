package org.example.ratingsystem.repositories;

import org.example.ratingsystem.entities.UserRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRankingRepository extends JpaRepository<UserRanking, UUID> {
}
