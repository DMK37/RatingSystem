package org.example.ratingsystem.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.auth.EmailVerificationDTO;
import org.example.ratingsystem.dtos.auth.SignUpRequestDTO;
import org.example.ratingsystem.dtos.auth.SignUpResponseDTO;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.entities.UserStatus;
import org.example.ratingsystem.enums.ApprovalStatus;
import org.example.ratingsystem.enums.Role;
import org.example.ratingsystem.exceptions.InvalidTokenException;
import org.example.ratingsystem.exceptions.UserAlreadyExistsException;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.repositories.UserStatusRepository;
import org.example.ratingsystem.services.interfaces.AuthService;
import org.example.ratingsystem.services.interfaces.EmailService;
import org.example.ratingsystem.services.interfaces.EmailValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${server.port}")
    private int port;

    @Value("${server.address:localhost}")
    private String address;

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailValidationService tokenValidationService;

    @Transactional
    @Override
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use!");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());

        User user = User.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        user = userRepository.save(user);
        UserStatus status = new UserStatus();
        status.setUser(user);
        status.setStatus(ApprovalStatus.PENDING);
        userStatusRepository.save(status);

        String token = UUID.randomUUID().toString();
        String verificationLink = String.format("http://%s:%d/auth/verify?token=%s", address, port, token);

        tokenValidationService.setToken(token, user.getId().toString());

        emailService.sendVerificationEmail(user.getEmail(), user.getFirstName(),
                verificationLink);

        return SignUpResponseDTO.builder()
                .message("Your account has been created and is pending approval.")
                .approvalStatus(ApprovalStatus.PENDING)
                .userId(user.getId())
                .nextSteps("Please check your email for verification.")
                .build();
    }

    @Override
    public EmailVerificationDTO verify(String token) {
        String id = tokenValidationService.getUserId(token);
        if (id == null) {
            throw new InvalidTokenException("Invalid token");
        }

        UserStatus userStatus = userStatusRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException("User status not found"));
        userStatus.setStatus(ApprovalStatus.EMAIL_VERIFIED);
        userStatusRepository.save(userStatus);

        return EmailVerificationDTO.builder()
                .message("Email verified")
                .nextSteps("Admin approval pending")
                .build();
    }
}
