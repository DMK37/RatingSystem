package org.example.ratingsystem.services.interfaces;

import org.example.ratingsystem.dtos.comment.CommentDTO;
import org.example.ratingsystem.dtos.comment.CommentRequestDTO;
import org.example.ratingsystem.dtos.comment.CommentResponseDTO;

import java.util.List;

public interface CommentService {
    CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO, String sellerId, String authorId);
    CommentDTO getCommentById(String commentId);
    void deleteComment(String commentId, String authorId);
    List<CommentDTO> getCommentsBySellerId(String sellerId);
    CommentResponseDTO updateComment(String commentId, CommentRequestDTO commentRequestDTO, String authorId);
}
