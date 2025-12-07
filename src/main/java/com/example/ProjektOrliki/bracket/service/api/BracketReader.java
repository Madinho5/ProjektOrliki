package com.example.ProjektOrliki.bracket.service.api;

import com.example.ProjektOrliki.bracket.dto.BracketDto;

public interface BracketReader {
    BracketDto getBracket(Long tournamentId);
}
