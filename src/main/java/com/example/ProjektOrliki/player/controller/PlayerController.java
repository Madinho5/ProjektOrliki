package com.example.ProjektOrliki.player.controller;

import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.dto.PlayerResponse;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.player.repository.PlayerRepository;
import com.example.ProjektOrliki.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerRepository playerRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse createPlayer(@RequestBody PlayerRequest request) {
        return playerService.createPlayer(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PlayerResponse updatePlayer(@PathVariable Long id, @RequestBody PlayerRequest request) {
        return playerService.updatePlayer(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.status(HttpStatus.OK).body("Usunięto zawodnika o id: " + id);
    }

}

