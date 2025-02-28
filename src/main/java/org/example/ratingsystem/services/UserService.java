package org.example.ratingsystem.services;

import org.example.ratingsystem.dtos.user.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO getUserById(UUID userId);
}
