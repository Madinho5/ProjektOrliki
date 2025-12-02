package com.example.ProjektOrliki.player.mapper;

import com.example.ProjektOrliki.player.dto.PlayerResponse;
import com.example.ProjektOrliki.player.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public PlayerResponse toResponse(Player p){
        return new PlayerResponse(
                p.getFirstName(),
                p.getLastName(),
                p.getAge(),
                p.getPosition(),
                p.getTeam().getName(),
                p.getTeam().getTrainer().getFirstName() + " " + p.getTeam().getTrainer().getLastName()
        );
    }
}
