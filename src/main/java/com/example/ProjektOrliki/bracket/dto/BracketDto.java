package com.example.ProjektOrliki.bracket.dto;

import lombok.Value;

import java.util.List;

@Value
public class BracketDto {
    List<BracketRoundDto> rounds;
}
