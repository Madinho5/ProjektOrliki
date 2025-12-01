package com.example.ProjektOrliki.tournament.controller;

import com.example.ProjektOrliki.tournament.dto.TournamentDetailsResponse;
import com.example.ProjektOrliki.match.dto.MatchDto;
import com.example.ProjektOrliki.match.repository.MatchRepository;
import com.example.ProjektOrliki.tournament.dto.*;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.bracket.service.BracketService;
import com.example.ProjektOrliki.tournament.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final BracketService bracketService;
    private final MatchRepository matchRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TournamentResponse create(@Valid @RequestBody TournamentRequest request) {
        return tournamentService.create(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TournamentDetailsResponse findById(@PathVariable Long id) {
        return tournamentService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TournamentResponse update(@PathVariable Long id, @Valid @RequestBody TournamentRequest request) {
        return tournamentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        tournamentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usunięto turniej o id: " + id );
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public TournamentResponse updateStatus(@PathVariable Long id, @RequestBody TournamentStatusRequest request) {
        return tournamentService.updateStatus(id,request.status());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TournamentResponse> getByStatus(
            @RequestParam TournamentStatus status
    ) {
        return tournamentService.getByStatus(status);
    }

    @PostMapping("/{id}/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String registerTeam(@PathVariable Long id) {
        tournamentService.registerTeam(id);
        return "Drużyna zgłoszona do turnieju.";
    }
}
