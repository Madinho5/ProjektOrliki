package com.example.ProjektOrliki.tournament.service;

import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.service.api.TournamentCreator;
import com.example.ProjektOrliki.tournament.service.api.TournamentImportHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentImportHandlerImpl implements TournamentImportHandler {

    private final TournamentCreator tournamentCreator;

    @Override
    public void handle(TournamentRequest request) {
        tournamentCreator.create(request);
    }
}
