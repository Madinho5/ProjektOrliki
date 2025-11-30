package com.example.ProjektOrliki.team.dto;

import com.example.ProjektOrliki.player.model.PlayerPosition;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.example.ProjektOrliki.team.model.Team}
 */
@Value
public class TeamDetailsResponse implements Serializable {
    Long id;
    String name;
    List<PlayerDto> players;

    /**
     * DTO for {@link com.example.ProjektOrliki.player.model.Player}
     */
    @Value
    public static class PlayerDto implements Serializable {
        Long id;
        String firstName;
        String lastName;
        Integer age;
        PlayerPosition position;
    }
}