package org.example.ratingsystem.services;

import org.example.ratingsystem.dtos.auth.SignUpRequestDTO;
import org.example.ratingsystem.dtos.auth.SignUpResponseDTO;

public interface AuthService {
    SignUpResponseDTO signUp(SignUpRequestDTO signUpRequest);
}
