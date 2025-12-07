package com.example.ProjektOrliki.bracket.controller;

import com.example.ProjektOrliki.bracket.service.api.BracketGenerator;
import com.example.ProjektOrliki.bracket.service.api.BracketReader;
import com.example.ProjektOrliki.match.dto.MatchDto;
import com.example.ProjektOrliki.bracket.dto.BracketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class BracketController {

    private final BracketReader reader;
    private final BracketGenerator generator;

    @GetMapping("/{id}/matches")
    public ResponseEntity<?> getBracket(@PathVariable Long id) {
        BracketDto dto = reader.getBracket(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/next-round")
    public ResponseEntity<?> generateNextRound(@PathVariable Long id) {
        List<MatchDto> matches = generator.generateNextRound(id);
        return ResponseEntity.ok(matches);
    }
}