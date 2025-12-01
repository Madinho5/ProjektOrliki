package com.example.ProjektOrliki;

import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import com.example.ProjektOrliki.player.model.Player;
import com.example.ProjektOrliki.player.model.PlayerPosition;
import com.example.ProjektOrliki.player.repository.PlayerRepository;
import com.example.ProjektOrliki.team.model.Team;
import com.example.ProjektOrliki.team.repository.TeamRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjektOrlikiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjektOrlikiApplication.class, args);
	}

    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            String username = "admin@gmail.com";

            if(userRepository.existsByUsername(username)) {
                return;
            }

            User admin = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .firstName("admin")
                    .lastName("admin")
                    .build();

            userRepository.save(admin);
        };
    }

    @Bean
    public CommandLineRunner seed(
            UserRepository userRepository,
            TeamRepository teamRepository,
            PlayerRepository playerRepository,
            PasswordEncoder encoder
    ) {
        return args -> {

            if (userRepository.existsByUsername("trainer1")) {
                return;
            }

            for (int i = 1; i <= 8; i++) {

                User trainer = User.builder()
                        .username("trainer" + i)
                        .firstName("Jan" + i)
                        .lastName("Kowalski" + i)
                        .password(encoder.encode("Password123!"))
                        .role(Role.TRAINER)
                        .build();
                userRepository.save(trainer);

                Team team = Team.builder()
                        .name("Team " + i)
                        .trainer(trainer)
                        .build();
                teamRepository.save(team);

                for (int p = 1; p <= 7; p++) {
                    Player player = Player.builder()
                            .firstName("Player" + i + "_" + p)
                            .lastName("Last" + i + "_" + p)
                            .age(18 + p)
                            .position(p == 1 ? PlayerPosition.GK : PlayerPosition.DEF)
                            .team(team)
                            .build();
                    playerRepository.save(player);
                }
            }
        };
    }
}
