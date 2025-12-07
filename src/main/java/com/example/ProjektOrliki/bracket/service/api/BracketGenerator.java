package com.example.ProjektOrliki.bracket.service.api;

import com.example.ProjektOrliki.match.dto.MatchDto;

import java.util.List;

public interface BracketGenerator {
    List<MatchDto> generateNextRound(Long tournamentId);
}
