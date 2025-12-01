package com.example.ProjektOrliki.match.model;

import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.tournament.model.Tournament;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer roundNumber;
    private Integer matchNumber;

    @ManyToOne
    private Team teamA;

    @ManyToOne
    private Team teamB;

    private Integer scoreA;
    private Integer scoreB;

    @ManyToOne
    private Team winner;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;
}
