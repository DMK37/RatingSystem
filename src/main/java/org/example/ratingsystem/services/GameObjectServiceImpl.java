package org.example.ratingsystem.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.dtos.gameobject.GameObjectDTO;
import org.example.ratingsystem.dtos.gameobject.GameObjectRequestDTO;
import org.example.ratingsystem.entities.GameObject;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.repositories.GameObjectRepository;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.services.interfaces.GameObjectService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameObjectServiceImpl implements GameObjectService {

    private final GameObjectRepository gameObjectRepository;
    private final UserRepository userRepository;

    @Override
    public GameObjectDTO getObjectById(String objectId) {
        GameObject gameObject = gameObjectRepository.findById(UUID.fromString(objectId)).orElseThrow(()
                -> new EntityNotFoundException("Object not found"));

        return GameObjectDTO.builder()
                .title(gameObject.getTitle())
                .text(gameObject.getText())
                .userId(gameObject.getUser().getId())
                .createdAt(gameObject.getCreatedAt())
                .build();
    }

    @Override
    public GameObjectDTO addGameObject(GameObjectRequestDTO gameObjectRequestDTO, String userId) {
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(()
                -> new EntityNotFoundException("User not found"));

        GameObject gameObject = GameObject.builder()
                .title(gameObjectRequestDTO.getTitle())
                .text(gameObjectRequestDTO.getText())
                .user(user)
                .build();

        gameObject = gameObjectRepository.save(gameObject);

        return GameObjectDTO.builder()
                .title(gameObject.getTitle())
                .text(gameObject.getText())
                .userId(gameObject.getUser().getId())
                .createdAt(gameObject.getCreatedAt())
                .build();
    }

    @Override
    public GameObjectDTO updateGameObject(String objectId, GameObjectRequestDTO gameObjectRequestDTO, String userId) {
        GameObject gameObject = gameObjectRepository.findById(UUID.fromString(objectId)).orElseThrow(()
                -> new EntityNotFoundException("Object not found"));

        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(()
                -> new EntityNotFoundException("User not found"));

        gameObject.setTitle(gameObjectRequestDTO.getTitle());
        gameObject.setText(gameObjectRequestDTO.getText());
        gameObject.setUser(user);

        gameObject = gameObjectRepository.save(gameObject);

        return GameObjectDTO.builder()
                .title(gameObject.getTitle())
                .text(gameObject.getText())
                .userId(gameObject.getUser().getId())
                .createdAt(gameObject.getCreatedAt())
                .build();
    }

    @Override
    public void deleteGameObject(String objectId, String userId) {
        GameObject gameObject = gameObjectRepository.findById(UUID.fromString(objectId)).orElseThrow(()
                -> new EntityNotFoundException("Object not found"));
        if (!gameObject.getUser().getId().equals(UUID.fromString(userId))) {
            throw new EntityNotFoundException("You are not the author of this object");
        }
        gameObjectRepository.deleteById(UUID.fromString(objectId));
    }
}
