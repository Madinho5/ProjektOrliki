package com.example.ProjektOrliki;

import com.example.ProjektOrliki.auth.model.Role;
import com.example.ProjektOrliki.auth.model.User;
import com.example.ProjektOrliki.auth.repository.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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
}
