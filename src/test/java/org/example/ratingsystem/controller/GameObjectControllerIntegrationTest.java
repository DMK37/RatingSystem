package org.example.ratingsystem.controller;

import org.example.ratingsystem.dtos.gameobject.GameObjectDTO;
import org.example.ratingsystem.entities.GameObject;
import org.example.ratingsystem.entities.User;
import org.example.ratingsystem.enums.Role;
import org.example.ratingsystem.repositories.GameObjectRepository;
import org.example.ratingsystem.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameObjectControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("rating_db_test")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GameObjectRepository gameObjectRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private GameObject testGameObject;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @BeforeEach
    void setUp() {
        // Clean repositories
        gameObjectRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setFirstName("First");
        testUser.setLastName("User");
        testUser.setRole(Role.USER);
        testUser = userRepository.save(testUser);

        // Create test game object
        testGameObject = GameObject.builder()
                .title("Test Game")
                .text("Test Description")
                .user(testUser)
                .createdAt(Instant.now().toEpochMilli())
                .build();
        testGameObject = gameObjectRepository.save(testGameObject);
    }

    @Test
    void getObjectById_ExistingObject_ReturnsObject() {
        // Arrange
        String url = "http://localhost:" + port + "/object/" + testGameObject.getId();

        // Act
        ResponseEntity<GameObjectDTO> response = restTemplate.getForEntity(url, GameObjectDTO.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        GameObjectDTO gameObjectDTO = response.getBody();
        assertEquals(testGameObject.getTitle(), gameObjectDTO.getTitle());
        assertEquals(testGameObject.getText(), gameObjectDTO.getText());
        assertEquals(testUser.getId(), gameObjectDTO.getUserId());
        assertEquals(testGameObject.getCreatedAt(), gameObjectDTO.getCreatedAt());
    }

    @Test
    void getObjectById_NonExistingObject_ReturnsNotFound() {
        // Arrange
        String nonExistingId = UUID.randomUUID().toString();
        String url = "http://localhost:" + port + "/object/" + nonExistingId;

        // Act
        ResponseEntity<GameObjectDTO> response = restTemplate.getForEntity(url, GameObjectDTO.class);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}