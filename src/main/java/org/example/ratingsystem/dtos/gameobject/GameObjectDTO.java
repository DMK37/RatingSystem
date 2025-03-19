package org.example.ratingsystem.dtos.gameobject;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameObjectDTO {
    private String title;
    private String text;
    private UUID userId;
    private long createdAt;
}
