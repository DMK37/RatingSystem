package org.example.ratingsystem.services.interfaces;

import org.example.ratingsystem.dtos.gameobject.GameObjectDTO;
import org.example.ratingsystem.dtos.gameobject.GameObjectRequestDTO;

public interface GameObjectService {
    GameObjectDTO getObjectById(String objectId);
    GameObjectDTO addGameObject(GameObjectRequestDTO gameObjectRequestDTO, String userId);
    GameObjectDTO updateGameObject(String objectId, GameObjectRequestDTO gameObjectRequestDTO, String userId);
    void deleteGameObject(String objectId, String userId);
}
