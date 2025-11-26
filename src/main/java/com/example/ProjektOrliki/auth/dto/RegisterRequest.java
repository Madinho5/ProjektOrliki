package com.example.ProjektOrliki.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.example.ProjektOrliki.auth.model.User}
 */
@Value
public class RegisterRequest implements Serializable {

    @NotBlank(message = "Nazwa nie może być pusta")
    String username;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])\\S{8,50}$",
            message = "Hasło musi mieć min. 8 znaków, wielką literę, małą literę, cyfrę i znak specjalny"
    )
    @NotBlank(message = "Hasło nie może być puste")
    String password;
}