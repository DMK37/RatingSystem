package org.example.ratingsystem.dtos.gameobject;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameObjectRequestDTO {
    @NotNull
    private String title;
    @NotNull
    private String text;
}
