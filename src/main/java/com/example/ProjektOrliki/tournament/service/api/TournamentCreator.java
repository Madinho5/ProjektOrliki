package com.example.ProjektOrliki.tournament.service.api;

import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface TournamentCreator {
    TournamentResponse create(@Valid TournamentRequest request);
}
