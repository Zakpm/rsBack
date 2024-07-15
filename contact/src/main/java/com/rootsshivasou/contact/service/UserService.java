package com.rootsshivasou.contact.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.rootsshivasou.contact.repository.UserRepository;
import com.rootsshivasou.moduleCommun.model.User;


@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;
    
    public User getUserById(int id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    // Méthode pour récupérer l'utilisateur administrateur
    public User getAdminUser() {
        // Récupérer les informations de l'utilisateur actuellement authentifié depuis le contexte de sécurité
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // Vérifier le rôle de l'utilisateur à partir de ses informations d'authentification (par exemple, à partir du jeton JWT)
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // Si l'utilisateur est un administrateur, vous pouvez récupérer ses autres informations à partir de votre système de stockage (par exemple, base de données)
            // Remplacez cette logique par la vôtre pour récupérer l'utilisateur administrateur
            return userRepository.findByNickname(userDetails.getUsername());
        } else {
            // Si l'utilisateur n'est pas un administrateur, retourner null
            return null;
        }
    }
}