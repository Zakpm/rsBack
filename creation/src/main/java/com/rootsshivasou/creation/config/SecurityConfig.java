package com.rootsshivasou.creation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rootsshivasou.creation.controller.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("Configuring SecurityFilterChain");
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF utile pour les API REST
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll() // pour permettre les requêtes des url externes
                                .requestMatchers(HttpMethod.POST,"/post/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/post/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/post/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers("/posts/**").permitAll()
                                .requestMatchers("/post/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/portfolio/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/portfolio/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/portfolio/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers("/portfolios/**").permitAll()
                                .requestMatchers("/portfolio/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/images/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/images/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/images/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE ,"/user/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.PUT ,"/user/**").hasAnyRole("ADMIN","SUPER_ADMIN", "USER","RESET_PASSWORD")
                                .requestMatchers("/users/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers("/user/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/category/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/category/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/category/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers("/category/**").permitAll()
                                .requestMatchers("/categories/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/fav/**").hasAnyRole("ADMIN","SUPER_ADMIN", "USER")
                                .requestMatchers("/fav/**").permitAll()
                                .requestMatchers("/favs/**").hasAnyRole("ADMIN","SUPER_ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST, "/appointment/**").hasAnyRole("ADMIN", "SUPER_ADMIN", "USER")
                                .requestMatchers("/appointment/pending/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                                .requestMatchers("/appointments/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/upload/image/**").permitAll()
                                .requestMatchers("/auth/**").permitAll() 
                                .requestMatchers("/verify/**").permitAll() 
                                .requestMatchers("/health/**").permitAll() 
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Ajouter le JwtAuthenticationFilter
        return http.build();
    }
    

}

