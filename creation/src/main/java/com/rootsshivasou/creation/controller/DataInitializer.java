package com.rootsshivasou.creation.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.rootsshivasou.creation.service.UserService;
import com.rootsshivasou.moduleCommun.enums.Genre;
import com.rootsshivasou.moduleCommun.enums.Role;
import com.rootsshivasou.moduleCommun.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService service; 

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override // methode qui va créer l'admin au lancement de l'api
    public void run(String... args) throws Exception {
        // Vérifier si l'utilisateur administrateur existe
        if (!service.existsByEmail("admin@gmail.com")) {
            User adminUser = new User();
            adminUser.setFirst_name("Admin");
            adminUser.setLast_name("ADMIN");
            adminUser.setNickname("admin");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("azerty1234A*"));
            adminUser.setDate_naissance(LocalDate.parse("1900-01-01"));
            adminUser.setGenre(Genre.FEMME);
            adminUser.setIs_verified(true);
            Set<Role> roles = new HashSet<>();
            roles.add(Role.valueOf("ROLE_USER"));
            roles.add(Role.valueOf("ROLE_ADMIN"));
            roles.add(Role.valueOf("ROLE_SUPER_ADMIN"));

            adminUser.setRoles(roles);
            service.saveUser(adminUser);
           
        }
    }
}
