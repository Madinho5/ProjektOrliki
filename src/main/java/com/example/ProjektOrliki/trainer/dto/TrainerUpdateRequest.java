package com.example.ProjektOrliki.trainer.dto;

import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.auth.model.User}
 */
public record TrainerUpdateRequest(
        @Pattern(
                regexp = "^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$",
                message = "Imię może zawierać tylko litery"
        )
        String firstName,

        @Pattern(
                regexp = "^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$",
                message = "Nazwisko może zawierać tylko litery"
        )
        String lastName,

        @Pattern(
                regexp = "^[0-9]{9}$",
                message = "Telefon musi mieć 9 cyfr"
        )
        String phoneNumber
) implements Serializable {}