package com.example.ProjektOrliki.tournament.service.api;

import com.example.ProjektOrliki.tournament.dto.TournamentRequest;

public interface TournamentImportHandler {
    void handle(TournamentRequest request);
}
