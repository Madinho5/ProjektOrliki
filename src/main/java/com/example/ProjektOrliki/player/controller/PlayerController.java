package com.example.ProjektOrliki.player.controller;

import com.example.ProjektOrliki.player.dto.PlayerRequest;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.player.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;
//    @PostMapping
//    private Player createPlayer(@RequestBody PlayerRequest request){
//        return playerService.createPlayer(
//                request.getTrainerId(),
//                request.getFirstName(),
//                request.getLastName(),
//                request.getAge(),
//                PlayerPosition.valueOf(request.getPosition())
//        );
//    }
//
//    @GetMapping("/trainer/{trainerId}")
//    public List<Player> getPlayerByTrainer(@PathVariable Long trainerId) {
//        return playerService.getPlayersByTrainer(trainerId);
//    }


}
