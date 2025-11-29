package com.example.ProjektOrliki.player.dto;

import com.example.ProjektOrliki.player.model.PlayerPosition;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.player.model.Player}
 */
@Value
public class PlayerResponse implements Serializable {
    String firstName;
    String lastName;
    Integer age;
    PlayerPosition position;
    String teamName;
    String teamTrainerFullName;
}