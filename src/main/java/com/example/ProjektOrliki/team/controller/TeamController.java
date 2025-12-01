package com.example.ProjektOrliki.team.controller;

import com.example.ProjektOrliki.team.dto.TeamRequest;
import com.example.ProjektOrliki.team.dto.TeamResponse;
import com.example.ProjektOrliki.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<?> createTeam(@Valid @RequestBody TeamRequest request) {
        TeamResponse response = teamService.createTeam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyTeam() {
        return ResponseEntity.ok(teamService.getMyTeam());
    }

    @PutMapping("/mine")
    @ResponseStatus(HttpStatus.OK)
    public TeamResponse updateMyTeam(@Valid @RequestBody TeamRequest request) {
        return teamService.updateMyTeam(request);
    }
    @DeleteMapping("/mine")
    public ResponseEntity<?> deleteMyTeam() {
        teamService.deleteMyTeam();
        return ResponseEntity.status(HttpStatus.OK).body("Usunięto drużynę");
    }
}
