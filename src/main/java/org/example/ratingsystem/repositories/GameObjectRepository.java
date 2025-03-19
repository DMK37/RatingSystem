package org.example.ratingsystem.repositories;

import org.example.ratingsystem.entities.GameObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GameObjectRepository extends JpaRepository<GameObject, UUID> {
}
