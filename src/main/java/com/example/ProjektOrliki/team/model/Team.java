package com.example.ProjektOrliki.team.model;

import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.player.model.Player;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private User trainer;

    @OneToMany(mappedBy = "team")
    private List<Player> players;
}
