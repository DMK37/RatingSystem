package org.example.ratingsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.auth.*;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @GetMapping("/forgot_password/{email}")
    public ResponseEntity<ForgotPasswordResponseDTO> forgotPassword(@PathVariable String email) {
        return ResponseEntity.ok(authService.forgotPassword(email));
    }

    @PostMapping("/reset_password")
    public ResponseEntity<ResetPasswordResponseDTO> resetPassword(@RequestBody
                                                                  ResetPasswordRequestDTO resetPasswordRequestDTO) {
        return ResponseEntity.ok(authService.resetPassword(resetPasswordRequestDTO));
    }

}
