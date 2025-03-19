package org.example.ratingsystem.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.user.UserDTO;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.entities.UserRanking;
import org.example.ratingsystem.repositories.UserRankingRepository;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRankingRepository userRankingRepository;

    @Override
    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() ->
                new EntityNotFoundException("User not found"));

        UserRanking ranking = userRankingRepository.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("User ranking not found"));

        UserDTO userDTO = UserDTO.mapUserToUserDTO(user);

        double ratio = (double) ranking.getPositiveCommentCount() / ranking.getCommentCount() * 100;
        userDTO.setRank((int)ratio);

        return userDTO;
    }
}
