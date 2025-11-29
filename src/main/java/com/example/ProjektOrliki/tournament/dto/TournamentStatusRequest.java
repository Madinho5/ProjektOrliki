package com.example.ProjektOrliki.tournament.dto;

import com.example.ProjektOrliki.tournament.model.TournamentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

public record  TournamentStatusRequest(TournamentStatus status) { }