package org.example.ratingsystem.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.config.auth.TokenProvider;
import org.example.ratingsystem.dtos.auth.*;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.entities.UserStatus;
import org.example.ratingsystem.enums.ApprovalStatus;
import org.example.ratingsystem.enums.Role;
import org.example.ratingsystem.exceptions.InvalidDataException;
import org.example.ratingsystem.exceptions.InvalidTokenException;
import org.example.ratingsystem.exceptions.LoginFailedException;
import org.example.ratingsystem.exceptions.UserAlreadyExistsException;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.repositories.UserStatusRepository;
import org.example.ratingsystem.services.interfaces.AuthService;
import org.example.ratingsystem.services.interfaces.EmailService;
import org.example.ratingsystem.services.interfaces.EmailValidationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${server.port}")
    private int port;

    @Value("${server.address}")
    private String address;

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final EmailValidationService tokenValidationService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    @Override
    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequest) {

        if (!isValidEmail(signUpRequest.getEmail())) {
            throw new InvalidDataException("Invalid email");
        }

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

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()));

        var user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new LoginFailedException("User not found"));

        if (user.getUserStatus().getStatus() != ApprovalStatus.APPROVED) {
            throw new LoginFailedException("User is not approved");
        }

        String token = tokenProvider.generateToken(loginRequestDTO.getEmail(),
                new HashMap<>() {{
                    put("role", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .findFirst()
                            .orElseThrow(() -> new LoginFailedException("User has no assigned roles!")));
                }});

        return LoginResponseDTO.builder()
                .token(token)
                .build();
    }
}
