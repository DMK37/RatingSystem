package org.example.ratingsystem.services.interfaces;

import org.example.ratingsystem.dtos.auth.EmailVerificationDTO;
import org.example.ratingsystem.dtos.auth.SignUpRequestDTO;
import org.example.ratingsystem.dtos.auth.SignUpResponseDTO;

public interface AuthService {
    SignUpResponseDTO signUp(SignUpRequestDTO signUpRequest);
    EmailVerificationDTO verify(String token);
}
