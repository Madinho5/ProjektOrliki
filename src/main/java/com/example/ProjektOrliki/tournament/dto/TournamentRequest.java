package com.example.ProjektOrliki.tournament.dto;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "Liczba druzyn jest wymagana")
    @Min(value = 2, message = "Turniej musi mieć minimum 2 drużyny")
    @Max(value = 16, message = "Turniej może mieć maksymalnie 16 drużyn")
    Integer teamCount;

}
