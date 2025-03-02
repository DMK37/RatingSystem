package org.example.ratingsystem.services.interfaces;

import org.example.ratingsystem.dtos.auth.*;

public interface AuthService {
    SignUpResponseDTO signUp(SignUpRequestDTO signUpRequest);
    EmailVerificationDTO verify(String token);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
