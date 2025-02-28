package org.example.ratingsystem.services;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.auth.SignUpRequestDTO;
import org.example.ratingsystem.dtos.auth.SignUpResponseDTO;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.entities.UserStatus;
import org.example.ratingsystem.enums.ApprovalStatus;
import org.example.ratingsystem.enums.Role;
import org.example.ratingsystem.exceptions.UserAlreadyExistsException;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.repositories.UserStatusRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final PasswordEncoder passwordEncoder;

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

        return SignUpResponseDTO.builder()
                .message("Your account has been created and is pending approval.")
                .approvalStatus(ApprovalStatus.PENDING)
                .userId(user.getId())
                .nextSteps("Please check your email for verification.")
                .build();
    }
}
