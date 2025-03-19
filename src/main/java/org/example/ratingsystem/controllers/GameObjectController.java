package org.example.ratingsystem.controllers;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.config.auth.TokenProvider;
import org.example.ratingsystem.dtos.gameobject.GameObjectDTO;
import org.example.ratingsystem.dtos.gameobject.GameObjectRequestDTO;
import org.example.ratingsystem.services.interfaces.GameObjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/object")
@RequiredArgsConstructor
public class GameObjectController {

    private final GameObjectService objectService;
    private final TokenProvider tokenProvider;

    @GetMapping("{objectId}")
    public ResponseEntity<GameObjectDTO> getObjectById(@PathVariable String objectId) {
        return ResponseEntity.ok(objectService.getObjectById(objectId));
    }

    @PostMapping()
    public ResponseEntity<GameObjectDTO> createObject(@RequestBody GameObjectRequestDTO gameObjectDTO,
                                                      @RequestHeader(name = "Authorization") String token) {
        String userId = extractUserId(token);

        return ResponseEntity.ok(objectService.addGameObject(gameObjectDTO, userId));
    }

    @PutMapping("{objectId}")
    public ResponseEntity<GameObjectDTO> updateObject(@PathVariable String objectId,
                                                      @RequestBody GameObjectRequestDTO gameObjectDTO,
                                                      @RequestHeader(name = "Authorization") String token) {
        String userId = extractUserId(token);

        return ResponseEntity.ok(objectService.updateGameObject(objectId, gameObjectDTO, userId));
    }

    @DeleteMapping("{objectId}")
    public ResponseEntity<Void> deleteObject(@PathVariable String objectId,
                                             @RequestHeader(name = "Authorization") String token) {
        String userId = extractUserId(token);

        objectService.deleteGameObject(objectId, userId);
        return ResponseEntity.ok().build();
    }

    private String extractUserId(String token) {
        if (token == null)
            return null;
        token = token.replace("Bearer ", "");
        return tokenProvider.extractUserId(token);
    }
}
