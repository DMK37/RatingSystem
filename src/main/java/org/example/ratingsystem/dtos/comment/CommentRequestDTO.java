package org.example.ratingsystem.dtos.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ratingsystem.enums.CommentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequestDTO {
    @NotNull
    private String message;
    @NotNull
    private CommentType type;
}
