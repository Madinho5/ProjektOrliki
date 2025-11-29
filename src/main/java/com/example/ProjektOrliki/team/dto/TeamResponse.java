package com.example.ProjektOrliki.team.dto;

import com.example.ProjektOrliki.team.model.Team;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Team}
 */
@Value
public class TeamResponse implements Serializable {
    Long id;
    String name;
    String trainerFullName;
    
}