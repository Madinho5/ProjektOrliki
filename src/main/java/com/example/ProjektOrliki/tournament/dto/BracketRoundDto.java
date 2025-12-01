package com.example.ProjektOrliki.tournament.dto;

import com.example.ProjektOrliki.match.dto.MatchDto;
import lombok.Value;

import java.util.List;

@Value
public class BracketRoundDto {
    Integer roundNumber;
    List<MatchDto> matches;
}
