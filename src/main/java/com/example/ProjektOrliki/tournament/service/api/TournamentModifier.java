package com.example.ProjektOrliki.tournament.service.api;

import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface TournamentModifier {
    TournamentResponse update(Long id, @Valid TournamentRequest request);
    void delete(Long id);
}
