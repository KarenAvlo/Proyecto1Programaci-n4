package com.example.proyecto1programacion4.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Security {
    //esta clase es para la encriptacion de contraseñas en el login


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // <--- ESTO ES VITAL para que el POST funcione sin tokens
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/registro/**", "/css/**", "/js/**", "/login", "/registro/guardar").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true) // Agregado para que sepa a dónde ir tras el login
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }

   /* @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .usernameParameter("username") //  aquí va el correo
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true") //  importante
                .permitAll();
    }*/
}
