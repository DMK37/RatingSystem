package org.example.ratingsystem.repositories;

import org.example.ratingsystem.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findAllBySellerId(UUID sellerId);
}
