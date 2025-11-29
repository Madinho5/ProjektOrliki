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
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/v3/api-docs/**",
                                    "/v2/api-docs/**",
                                    "/swagger-resources/**",
                                    "/webjars/**").permitAll()

                            .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                            .requestMatchers(HttpMethod.POST,"/auth/login").permitAll()

                            .requestMatchers(HttpMethod.POST,"/tournaments/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT,"/tournaments/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE,"/tournaments/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,"/tournaments/**").authenticated()

                            .requestMatchers(HttpMethod.POST, "/teams/**").hasRole("TRAINER")
                            .anyRequest().authenticated()
                    )
                    .userDetailsService(userDetailsService)
                    .httpBasic(Customizer.withDefaults());

            return http.build();
        }
    }
