package com.example.ProjektOrliki.tournament.service;

import com.example.ProjektOrliki.team.dto.TeamResponse;
import com.example.ProjektOrliki.tournament.dto.TournamentRequest;
import com.example.ProjektOrliki.tournament.dto.TournamentResponse;
import com.example.ProjektOrliki.tournament.model.Tournament;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<TournamentResponse> getByStatus(TournamentStatus status) {
        return repository.findByStatus(status).stream()
                .map(this::toResponse)
                .toList();
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

    public TournamentResponse updateStatus(Long id, TournamentStatus newStatus) {
        Tournament tournament = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono turnieju o id: " + id));

        tournament.setStatus(newStatus);

        return toResponse(repository.save(tournament));
    }
    private TournamentResponse toResponse(Tournament t) {
        return new TournamentResponse(
                t.getId(),
                t.getName(),
                t.getStartDate(),
                t.getStatus(),
                t.getTeamCount()
        );
    }

}
