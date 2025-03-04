package org.example.ratingsystem.dtos.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDTO {
    private UUID id;
    private String message;
    private long createdAt;
    private String nextStep;
}
