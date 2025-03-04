package org.example.ratingsystem.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ratingsystem.dtos.user.UserDTO;
import org.example.ratingsystem.entities.Comment;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private UUID id;
    private String message;
    private long createdAt;
    private UserDTO seller;
    private UserDTO author;

    public static CommentDTO mapCommentToCommentDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .createdAt(comment.getCreatedAt())
                .seller(UserDTO.builder()
                        .id(comment.getSeller().getId())
                        .firstName(comment.getSeller().getFirstName())
                        .lastName(comment.getSeller().getLastName())
                        .build())
                .author(UserDTO.builder()
                        .id(comment.getAuthor().getId())
                        .firstName(comment.getAuthor().getFirstName())
                        .lastName(comment.getAuthor().getLastName())
                        .build())
                .build();
    }

    public static CommentDTO mapBatchCommentToCommentDTO(Comment comment) {
        var builder =  CommentDTO.builder()
                .id(comment.getId())
                .message(comment.getMessage())
                .createdAt(comment.getCreatedAt());
        if (comment.getAuthor() != null) {
            builder.author(UserDTO.builder()
                    .id(comment.getAuthor().getId())
                    .firstName(comment.getAuthor().getFirstName())
                    .lastName(comment.getAuthor().getLastName())
                    .build());
        }

        return builder.build();
    }



    public static List<CommentDTO> mapCommentListToCommentDTOList(List<Comment> comments) {
        return comments.stream().map(CommentDTO::mapBatchCommentToCommentDTO).collect(Collectors.toList());
    }
}
