package com.example.ProjektOrliki.player.dto;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.player.model.Player}
 */
@Value
public class PlayerRequest implements Serializable {

    @Pattern(
            regexp = "^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$",
            message = "Imię może zawierać tylko litery."
    )
    @NotBlank(message = "Imię nie może być puste.")
    String firstName;

    @Pattern(
            regexp = "^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ]+$",
            message = "Nazwisko może zawierać tylko litery."
    )
    @NotBlank(message = "Nazwisko nie może być puste.")
    String lastName;

    @NotNull(message = "Wiek jest wymagany.")
    @Min(value = 5, message = "Minimalny wiek to 5 lat.")
    @Max(value = 80, message = "Maksymalny wiek to 80 lat.")
    Integer age;

    @NotBlank(message = "Pozycja jest wymagana.")
    String position;
}