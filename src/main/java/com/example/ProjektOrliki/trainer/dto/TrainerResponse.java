package com.example.ProjektOrliki.trainer.dto;

import com.example.ProjektOrliki.trainer.model.Trainer;

import java.io.Serializable;

/**
 * DTO for {@link Trainer}
 */
public record TrainerResponse(long id, String firstName, String lastName, String phoneNumber) implements Serializable {
  public TrainerResponse(Trainer t) {
    this(
            t.getId(),
            t.getFirstName(),
            t.getLastName(),
            t.getPhoneNumber()
    );
  }
}