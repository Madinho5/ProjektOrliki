package com.example.ProjektOrliki.player.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.player.model.Player}
 */
@Value
public class PlayerRequest implements Serializable {
    String firstName;
    String lastName;
    Integer age;
    String position;
    Long trainerId;
}