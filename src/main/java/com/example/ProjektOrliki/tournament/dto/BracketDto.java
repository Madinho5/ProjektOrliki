package com.example.ProjektOrliki.tournament.dto;

import lombok.Value;

import java.util.List;

@Value
public class BracketDto {
    List<BracketRoundDto> rounds;
}
