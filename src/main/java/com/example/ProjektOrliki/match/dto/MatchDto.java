package com.example.ProjektOrliki.match.dto;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.match.model.Match}
 */
@Value
public class MatchDto implements Serializable {
    Long id;
    Integer matchNumber;
    String teamAName;
    String teamBName;
    Integer scoreA;
    Integer scoreB;
    String winnerName;
}