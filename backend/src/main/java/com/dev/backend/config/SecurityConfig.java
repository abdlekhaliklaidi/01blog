package com.dev.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            // .authorizeHttpRequests(auth -> auth
            //     .requestMatchers("/users/register", "/users/login").permitAll()
            //     .anyRequest().authenticated()
            // )
            .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()
            )

            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
