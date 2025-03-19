package org.example.ratingsystem.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.comment.CommentDTO;
import org.example.ratingsystem.dtos.comment.CommentRequestDTO;
import org.example.ratingsystem.dtos.comment.CommentResponseDTO;
import org.example.ratingsystem.entities.Comment;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.entities.UserRanking;
import org.example.ratingsystem.enums.CommentType;
import org.example.ratingsystem.exceptions.InvalidDataException;
import org.example.ratingsystem.repositories.CommentRepository;
import org.example.ratingsystem.repositories.UserRankingRepository;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.services.interfaces.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserRankingRepository userRankingRepository;

    @Override
    @Transactional
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

        UserRanking ranking = userRankingRepository.findById(seller.getId()).orElseThrow(() ->
                new EntityNotFoundException("User ranking not found"));

        ranking.setCommentCount(ranking.getCommentCount() + 1);
        if (commentRequestDTO.getType() == CommentType.POSITIVE) {
            ranking.setPositiveCommentCount(ranking.getPositiveCommentCount() + 1);
        }

        userRankingRepository.save(ranking);

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
    @Transactional
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

        UserRanking ranking = userRankingRepository.findById(comment.getSeller().getId()).orElseThrow(() ->
                new EntityNotFoundException("User ranking not found"));

        if (comment.isPositive()) {
            ranking.setPositiveCommentCount(ranking.getPositiveCommentCount() - 1);
        }

        ranking.setCommentCount(ranking.getCommentCount() - 1);
        userRankingRepository.save(ranking);

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
                .isPositive(commentRequestDTO.getType() == CommentType.POSITIVE)
                .createdAt(comment.getCreatedAt())
                .approved(false)
                .build();

        updatedComment = commentRepository.save(updatedComment);

        UserRanking ranking = userRankingRepository.findById(comment.getSeller().getId()).orElseThrow(() ->
                new EntityNotFoundException("User ranking not found"));

        if (comment.isPositive() != updatedComment.isPositive()) {
            if (comment.isPositive()) {
                ranking.setPositiveCommentCount(ranking.getPositiveCommentCount() - 1);
            } else {
                ranking.setPositiveCommentCount(ranking.getPositiveCommentCount() + 1);
            }
        }

        userRankingRepository.save(ranking);

        return CommentResponseDTO.builder()
                .id(updatedComment.getId())
                .message(updatedComment.getMessage())
                .createdAt(updatedComment.getCreatedAt())
                .nextStep("Wait for approval")
                .build();
    }
}
