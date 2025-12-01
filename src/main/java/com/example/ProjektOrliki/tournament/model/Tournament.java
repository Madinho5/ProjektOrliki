package com.example.ProjektOrliki.tournament.model;

import com.example.ProjektOrliki.team.model.Team;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    private TournamentStatus status;

    private Integer teamCount;

    @ManyToMany
    @JoinTable(
            name="tournament_teams",
            joinColumns = @JoinColumn(name="tournament_id"),
            inverseJoinColumns = @JoinColumn(name="team_id")
    )
    private List<Team> teams;

    @ManyToOne
    @JoinColumn(name="winner_id")
    private Team winner;

    private Integer currentRound;
}
