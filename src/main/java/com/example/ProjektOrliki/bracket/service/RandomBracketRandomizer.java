package com.example.ProjektOrliki.bracket.service;

import com.example.ProjektOrliki.bracket.service.api.BracketRandomizer;
import com.example.ProjektOrliki.team.model.Team;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomBracketRandomizer implements BracketRandomizer {
    private final Random random = new Random();

    @Override
    public int generateScore(Team team) {
        return random.nextInt(6);
    }
}
