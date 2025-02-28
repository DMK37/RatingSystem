package org.example.ratingsystem.services;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.user.UserDTO;
import org.example.ratingsystem.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;


    @Override
    public UserDTO getUserById(UUID userId) {
        return null;
    }
}
