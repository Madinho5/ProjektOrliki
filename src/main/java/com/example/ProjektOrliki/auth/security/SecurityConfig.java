    package com.example.ProjektOrliki.auth.security;

    import com.example.ProjektOrliki.auth.service.UserDetailsServiceImpl;
    import lombok.RequiredArgsConstructor;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.Customizer;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;

    @Configuration
    @RequiredArgsConstructor
    public class SecurityConfig {

        private final UserDetailsServiceImpl userDetailsService;
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth

                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/swagger-resources/**",
                                    "/webjars/**"
                            ).permitAll()

                            .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()

                            .requestMatchers(HttpMethod.POST, "/tournaments").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/tournaments/*").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/tournaments/*").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/tournaments/*/status").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.POST, "/tournaments/import").hasRole("ADMIN")

                            .requestMatchers(HttpMethod.GET, "/tournaments/**").authenticated()
                            .requestMatchers(HttpMethod.POST, "/tournaments/*/register").hasRole("TRAINER")
                            .requestMatchers(HttpMethod.POST, "/tournaments/*/next-round").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/tournaments/*/matches").authenticated()

                            .requestMatchers("/teams/mine").hasRole("TRAINER")
                            .requestMatchers(HttpMethod.POST, "/teams").hasRole("TRAINER")
                            .requestMatchers(HttpMethod.PUT, "/teams/mine").hasRole("TRAINER")
                            .requestMatchers(HttpMethod.DELETE, "/teams/mine").hasRole("TRAINER")

                            .requestMatchers(HttpMethod.POST, "/players").hasRole("TRAINER")
                            .requestMatchers(HttpMethod.PUT, "/players/*").hasRole("TRAINER")
                            .requestMatchers(HttpMethod.DELETE, "/players/*").hasRole("TRAINER")

                            .requestMatchers(HttpMethod.GET, "/trainer/me").hasRole("TRAINER")
                            .requestMatchers(HttpMethod.PUT, "/trainer/me").hasRole("TRAINER")

                            .anyRequest().authenticated()
                    )
                    .userDetailsService(userDetailsService)
                    .httpBasic(Customizer.withDefaults());

            return http.build();
        }
    }
