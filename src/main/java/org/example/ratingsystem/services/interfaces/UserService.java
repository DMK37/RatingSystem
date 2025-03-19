package org.example.ratingsystem.services.interfaces;

import org.example.ratingsystem.dtos.user.UserDTO;

public interface UserService {
    UserDTO getUserById(String userId);
}
