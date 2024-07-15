package com.rootsshivasou.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rootsshivasou.security.service.ResetPasswordRequestService;

@RestController
@RequestMapping("/reset-password")
@CrossOrigin(origins = "*") // URL qui feront les requêtes
public class ResetPasswordController {

    @Autowired
    private ResetPasswordRequestService resetPasswordRequestService;

    /**
     * Endpoint pour demander la réinitialisation du mot de passe.
     *
     * @param emailWrapper Un objet contenant l'e-mail de l'utilisateur.
     * @return Une réponse indiquant si la demande a été traitée.
     */
    @PostMapping
    public ResponseEntity<?> requestPasswordReset(@RequestBody EmailWrapper emailWrapper) {
        try {
            resetPasswordRequestService.createResetPasswordRequest(emailWrapper.getEmail());
            return ResponseEntity.ok().body("Demande de réinitialisation du mot de passe envoyée.");
        } catch (Exception e) {
            // Gérer l'exception si nécessaire, par exemple, enregistrer un log
            return ResponseEntity.internalServerError().body("Erreur lors de la demande de réinitialisation du mot de passe.");
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    /**
     * Classe wrapper pour l'email.
     */
    private static class EmailWrapper {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
