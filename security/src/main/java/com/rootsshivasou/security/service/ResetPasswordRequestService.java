package com.rootsshivasou.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import com.rootsshivasou.security.repository.UserRepository;
import com.rootsshivasou.security.controller.JwtTokenProvider;
import com.rootsshivasou.security.repository.ResetPasswordRequestRepository;
import com.rootsshivasou.moduleCommun.model.User;
import com.rootsshivasou.moduleCommun.model.ResetPasswordRequest;
import com.rootsshivasou.security.service.email.EmailService;

@Service
public class ResetPasswordRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetPasswordRequestRepository resetPasswordRequestRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${app.front.url}")
    private String frontUrl;

    public void createResetPasswordRequest(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Générer le JWT pour la réinitialisation
            List<String> roles = List.of("ROLE_RESET_PASSWORD");

            String resetToken = jwtTokenProvider.createToken(user.getEmail(), roles, user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getId());
    
            // Créer et enregistrer la demande de réinitialisation dans la base de données
            ResetPasswordRequest resetRequest = new ResetPasswordRequest();
            resetRequest.setUserRequest(user);
            resetRequest.setToken(resetToken);
            resetRequest.setExpiresAt(LocalDateTime.now().plusHours(24)); // Exemple d'expiration dans 24 heures
            resetPasswordRequestRepository.save(resetRequest);
    
            // Construire le lien de réinitialisation
            String resetLink = frontUrl + "/reset-password/index?token=" + resetToken + "&id=" + user.getId();;
            
            // Envoyer l'e-mail avec le lien de réinitialisation formaté en HTML
            emailService.sendEmail(user.getEmail(), "Réinitialisation de votre mot de passe", resetLink);
        } else {
            
        }
    }
    
}
