package com.example.ProjektOrliki.tournament.service;

import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TournamentService {
    private final TournamentRepository repository;

    public TournamentResponse create(TournamentRequest request) {
        if(repository.existsByName(request.getName())){
            throw new IllegalArgumentException("Turniej o takiej nazwie juz istnieje");
        }

        Tournament tournament = Tournament.builder()
                .name(request.getName())
                .startDate(request.getStartDate())
                .status(TournamentStatus.CREATED)
                .teamCount(request.getTeamCount())
                .build();
        Tournament savedTournament = repository.save(tournament);
        return toResponse(savedTournament);
    }
    public TournamentResponse getById(Long id) {
            Tournament tournament = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));
        return toResponse(tournament);
    }

    public TournamentResponse update(Long id, TournamentRequest request) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));

        if(!tournament.getName().equals(request.getName()) && repository.existsByName(request.getName())){
            throw new IllegalArgumentException("Turniej o takiej nazwie juz istnieje");
        }

        tournament.setName(request.getName());
        tournament.setStartDate(request.getStartDate());
        tournament.setTeamCount(request.getTeamCount());

        Tournament updatedTournament = repository.save(tournament);
        return toResponse(updatedTournament);
    }
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Nie znaleziono turnieju o id: " + id);
        }
        repository.deleteById(id);
    }

    private TournamentResponse toResponse(Tournament t) {
        return new TournamentResponse(
                t.getName(),
                t.getStartDate(),
                t.getStatus(),
                t.getTeamCount()
        );
    }
}
