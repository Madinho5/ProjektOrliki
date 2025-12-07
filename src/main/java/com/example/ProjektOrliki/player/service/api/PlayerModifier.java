package com.example.ProjektOrliki.player.service.api;

import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.dto.PlayerResponse;
import com.example.ProjektOrliki.player.service.PlayerService;

public interface PlayerModifier {
    PlayerResponse updatePlayer(Long id, PlayerRequest request);
}
