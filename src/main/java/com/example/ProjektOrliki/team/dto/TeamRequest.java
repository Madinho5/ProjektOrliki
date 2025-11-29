package com.example.ProjektOrliki.team.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.team.model.Team}
 */
@Value
public class TeamRequest implements Serializable {
    @NotBlank(message = "Nazwa nie może być pusta.")
    String name;
}