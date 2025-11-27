package com.example.ProjektOrliki.trainer.repository;

import com.example.ProjektOrliki.trainer.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUserId(Long userId);
}
