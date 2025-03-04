package org.example.ratingsystem.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.comment.CommentDTO;
import org.example.ratingsystem.dtos.comment.CommentRequestDTO;
import org.example.ratingsystem.dtos.comment.CommentResponseDTO;
import org.example.ratingsystem.entities.Comment;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.exceptions.InvalidDataException;
import org.example.ratingsystem.repositories.CommentRepository;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.services.interfaces.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponseDTO addComment(CommentRequestDTO commentRequestDTO, String sellerId, String authorId) {

        User author = null;
        if (authorId != null && !authorId.isEmpty()) {
            author = userRepository.findById(UUID.fromString(authorId)).orElseThrow(() ->
                    new EntityNotFoundException("User not found"));
        }

        User seller = userRepository.findById(UUID.fromString(sellerId)).orElseThrow(() ->
                new EntityNotFoundException("User not found"));

        Comment comment = Comment.builder()
                .author(author)
                .seller(seller)
                .message(commentRequestDTO.getMessage())
                .build();
        comment = commentRepository.save(comment);

        return CommentResponseDTO.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .createdAt(comment.getCreatedAt())
                .nextStep("Wait for approval")
                .build();
    }

    @Override
    public CommentDTO getCommentById(String commentId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId)).orElseThrow(() ->
                new EntityNotFoundException("Comment not found"));

        return CommentDTO.mapCommentToCommentDTO(comment);
    }

    @Override
    public void deleteComment(String commentId, String authorId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId)).orElseThrow(() ->
                new EntityNotFoundException("Comment not found"));
        if (comment.getAuthor() == null) {
            throw new InvalidDataException("Comment author not found");
        }
        User author = userRepository.findById(UUID.fromString(authorId)).orElseThrow(() ->
                new EntityNotFoundException("User not found"));

        if (!comment.getAuthor().getId().equals(author.getId())) {
            throw new InvalidDataException("You are not the author of this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDTO> getCommentsBySellerId(String sellerId) {
        List<Comment> comments = commentRepository.findAllBySellerId(UUID.fromString(sellerId));

        return CommentDTO.mapCommentListToCommentDTOList(comments);
    }

    @Override
    public CommentResponseDTO updateComment(String commentId, CommentRequestDTO commentRequestDTO, String authorId) {
        Comment comment = commentRepository.findById(UUID.fromString(commentId)).orElseThrow(() ->
                new EntityNotFoundException("Comment not found"));

        if (comment.getAuthor() == null) {
            throw new InvalidDataException("Comment author not found");
        }

        Comment updatedComment = Comment.builder()
                .id(comment.getId())
                .author(comment.getAuthor())
                .seller(comment.getSeller())
                .message(commentRequestDTO.getMessage())
                .createdAt(comment.getCreatedAt())
                .approved(false)
                .build();

        updatedComment = commentRepository.save(updatedComment);

        return CommentResponseDTO.builder()
                .id(updatedComment.getId())
                .message(updatedComment.getMessage())
                .createdAt(updatedComment.getCreatedAt())
                .nextStep("Wait for approval")
                .build();
    }
}
