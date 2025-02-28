package org.example.ratingsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.auth.SignUpRequestDTO;
import org.example.ratingsystem.dtos.auth.SignUpResponseDTO;
import org.example.ratingsystem.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        System.out.println("Sign up request received");
        return ResponseEntity.status(201).body(authService.signUp(signUpRequestDTO));
    }
}
