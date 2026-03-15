package com.example.proyecto1programacion4.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Security {
    //esta clase es para la encriptacion de contraseñas en el login


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF para pruebas
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite entrar a todas las rutas sin login
                );
        return http.build();
    }
}
