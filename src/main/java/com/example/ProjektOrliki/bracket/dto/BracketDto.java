package com.example.ProjektOrliki.bracket.dto;

import lombok.Value;

import java.io.Serializable;
import java.util.List;

@Value
public class BracketDto implements Serializable {
    List<BracketRoundDto> rounds;
}
