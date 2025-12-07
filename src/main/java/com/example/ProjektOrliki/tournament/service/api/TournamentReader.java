package com.example.ProjektOrliki.tournament.service.api;

import com.example.ProjektOrliki.tournament.dto.TournamentDetailsResponse;
import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;

import java.util.List;

public interface TournamentReader {
    TournamentDetailsResponse getById(Long id);
    List<TournamentResponse> getByStatus(TournamentStatus status);
}
