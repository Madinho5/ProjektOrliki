package com.example.ProjektOrliki.tournament.service.api;

import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;

public interface TournamentLifecycle {
    TournamentResponse updateStatus(Long id, TournamentStatus newStatus);
}
