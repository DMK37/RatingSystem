package org.example.ratingsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.auth.EmailVerificationDTO;
import org.example.ratingsystem.dtos.auth.SignUpRequestDTO;
import org.example.ratingsystem.dtos.auth.SignUpResponseDTO;
import org.example.ratingsystem.services.interfaces.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {

        return ResponseEntity.status(201).body(authService.signUp(signUpRequestDTO));
    }

    @GetMapping("/verify")
    public ResponseEntity<EmailVerificationDTO> verifyEmail(@RequestParam String token) {

        return ResponseEntity.ok(authService.verify(token));
    }
}
