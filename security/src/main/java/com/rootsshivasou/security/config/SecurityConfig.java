package com.rootsshivasou.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rootsshivasou.security.controller.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF, utile pour les API REST
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/auth/**").permitAll() // Autoriser toutes les requêtes
                                .requestMatchers("/reset-password/**").permitAll() // Autoriser toutes les requêtes
                                .anyRequest().authenticated()
                )
                .httpBasic(basic -> basic.disable()) // Désactiver l'authentification de base HTTP
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();

    }
}