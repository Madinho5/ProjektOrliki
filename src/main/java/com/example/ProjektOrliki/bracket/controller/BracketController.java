package com.example.ProjektOrliki.bracket.controller;

import com.example.ProjektOrliki.bracket.service.BracketService;
import com.example.ProjektOrliki.match.dto.MatchDto;
import com.example.ProjektOrliki.bracket.dto.BracketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tournaments")
@RequiredArgsConstructor
public class BracketController {

    private final BracketService bracketService;

    @GetMapping("/{id}/matches")
    @ResponseStatus(HttpStatus.OK)
    public BracketDto getBracket(@PathVariable Long id) {
        return bracketService.getBracket(id);
    }

    @PostMapping("/{id}/next-round")
    @ResponseStatus(HttpStatus.OK)
    public List<MatchDto> generateNextRound(@PathVariable Long id) {
        return bracketService.generateNextRound(id);
    }
}