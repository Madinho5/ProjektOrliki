package com.example.ProjektOrliki.bracket.controller;

import com.example.ProjektOrliki.bracket.service.BracketService;
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

    private final BracketService bracketService;

    @GetMapping("/{id}/matches")
    public ResponseEntity<?> getBracket(@PathVariable Long id) {
        BracketDto dto = bracketService.getBracket(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/next-round")
    public ResponseEntity<?> generateNextRound(@PathVariable Long id) {
        List<MatchDto> matches = bracketService.generateNextRound(id);
        return ResponseEntity.ok(matches);
    }
}