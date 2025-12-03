package com.example.ProjektOrliki.tournament.controller;

import com.example.ProjektOrliki.tournament.dto.TournamentDetailsResponse;
import com.example.ProjektOrliki.tournament.dto.*;
import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import com.example.ProjektOrliki.tournament.service.TournamentImportService;
import com.example.ProjektOrliki.tournament.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
@Validated
public class TournamentController {

    private final TournamentService tournamentService;
    private final TournamentImportService tournamentImportService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TournamentRequest request) {
        TournamentResponse response = tournamentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        TournamentDetailsResponse response = tournamentService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody TournamentRequest request) {
        TournamentResponse response = tournamentService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        tournamentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usunięto turniej o id: " + id );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody TournamentStatusRequest request) {
        TournamentResponse response = tournamentService.updateStatus(id, request.status());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getByStatus(@RequestParam TournamentStatus status) {
        List<TournamentResponse> responses = tournamentService.getByStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<?> registerTeam(@PathVariable Long id) {
        tournamentService.registerTeam(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("Drużyna zgłoszona do turnieju");
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ResponseEntity<?> importXml(@RequestParam MultipartFile file) throws IOException {
        ImportResultDto result = tournamentImportService.importXml(file.getInputStream());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
