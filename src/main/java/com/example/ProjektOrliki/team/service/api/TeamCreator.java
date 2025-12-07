package com.example.ProjektOrliki.team.service.api;

import com.example.ProjektOrliki.team.dto.TeamRequest;
import com.example.ProjektOrliki.team.dto.TeamResponse;
import jakarta.validation.Valid;

public interface TeamCreator {
    TeamResponse create(@Valid TeamRequest request);
}