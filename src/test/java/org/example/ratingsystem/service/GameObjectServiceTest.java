package org.example.ratingsystem.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.ratingsystem.dtos.gameobject.GameObjectDTO;
import org.example.ratingsystem.dtos.gameobject.GameObjectRequestDTO;
import org.example.ratingsystem.entities.GameObject;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.repositories.GameObjectRepository;
import org.example.ratingsystem.repositories.UserRepository;
import org.example.ratingsystem.services.GameObjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameObjectServiceTest {

    @Mock
    private GameObjectRepository gameObjectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameObjectServiceImpl gameObjectService;

    private UUID objectId;
    private UUID userId;
    private User user;
    private GameObject gameObject;
    private GameObjectRequestDTO requestDTO;

    @BeforeEach
    public void setUp() {
        objectId = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = new User();
        user.setId(userId);

        gameObject = GameObject.builder()
                .id(objectId)
                .title("Test Game")
                .text("Test Description")
                .user(user)
                .createdAt(Instant.now().toEpochMilli())
                .build();

        requestDTO = new GameObjectRequestDTO();
        requestDTO.setTitle("New Game");
        requestDTO.setText("New Description");
    }

    @Test
    public void getObjectById_ValidId_ReturnsGameObjectDTO() {
        // Arrange
        when(gameObjectRepository.findById(objectId)).thenReturn(Optional.of(gameObject));

        // Act
        GameObjectDTO result = gameObjectService.getObjectById(objectId.toString());

        // Assert
        assertNotNull(result);
        assertEquals(gameObject.getTitle(), result.getTitle());
        assertEquals(gameObject.getText(), result.getText());
        assertEquals(gameObject.getUser().getId(), result.getUserId());
        assertEquals(gameObject.getCreatedAt(), result.getCreatedAt());
        verify(gameObjectRepository).findById(objectId);
    }

    @Test
    public void getObjectById_InvalidId_ThrowsEntityNotFoundException() {
        // Arrange
        when(gameObjectRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            gameObjectService.getObjectById(objectId.toString());
        });
    }

    @Test
    public void addGameObject_ValidData_ReturnsGameObjectDTO() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(gameObjectRepository.save(any(GameObject.class))).thenAnswer(invocation -> {
            GameObject savedObject = invocation.getArgument(0);
            savedObject.setId(objectId);
            savedObject.setCreatedAt(Instant.now().toEpochMilli());
            return savedObject;
        });

        // Act
        GameObjectDTO result = gameObjectService.addGameObject(requestDTO, userId.toString());

        // Assert
        assertNotNull(result);
        assertEquals(requestDTO.getTitle(), result.getTitle());
        assertEquals(requestDTO.getText(), result.getText());
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getCreatedAt());

        verify(userRepository).findById(userId);
        verify(gameObjectRepository).save(any(GameObject.class));
    }

    @Test
    public void addGameObject_InvalidUserId_ThrowsEntityNotFoundException() {
        // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            gameObjectService.addGameObject(requestDTO, userId.toString());
        });
    }
}