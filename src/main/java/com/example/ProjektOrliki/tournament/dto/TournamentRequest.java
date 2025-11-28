package com.example.ProjektOrliki.tournament.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.example.ProjektOrliki.tournament.model.Tournament}
 */
@Value
public class TournamentRequest implements Serializable {
    @NotBlank(message = "Nazwa nie moze byc pusta")
    String name;

    @NotNull(message = "Data jest wymagana")
    @FutureOrPresent
    LocalDate startDate;
}
