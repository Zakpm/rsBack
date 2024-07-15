package com.rootsshivasou.contact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF si nécessaire, utile pour les API REST
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/contact/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/contact/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers("/contact/**").permitAll()
                                .requestMatchers("/contacts/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.GET,"/messages/**").hasAnyRole("ADMIN","SUPER_ADMIN", "USER")
                                .requestMatchers(HttpMethod.DELETE,"/message/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                                .requestMatchers(HttpMethod.POST,"/message/**").hasAnyRole("ADMIN","SUPER_ADMIN", "USER")
                                .requestMatchers(HttpMethod.PUT,"/message/**").hasAnyRole("ADMIN","SUPER_ADMIN", "USER")
                                .requestMatchers(HttpMethod.POST,"/chat/**").hasAnyRole("ADMIN","SUPER_ADMIN", "USER")
                                .requestMatchers("/health/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // Ajouter le JwtAuthenticationFilter
        return http.build();
    }
    

}