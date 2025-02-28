package org.example.ratingsystem.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ratingsystem.enums.ApprovalStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpResponseDTO {
    private String message;
    private ApprovalStatus approvalStatus;
    private UUID userId;
    private String nextSteps;
}
