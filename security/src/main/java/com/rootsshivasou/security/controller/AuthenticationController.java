package com.rootsshivasou.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rootsshivasou.security.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // URL qui feront des requêtes
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authenticationService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed");
        }
    }

    public static class LoginRequest {
        private String login;
        private String password;
    
        // Getter et setter pour 'login'
        public String getLogin() {
            return login;
        }
    
        public void setLogin(String login) {
            this.login = login;
        }
    
        // Getter et setter pour 'password'
        public String getPassword() {
            return password;
        }
    
        public void setPassword(String password) {
            this.password = password;
        }
    }
    

    // Réponse JWT
    public static class JwtResponse {
        private String jwt;

        public JwtResponse(String jwt) {
            this.jwt = jwt;
        }

        public String getJwt() {
            return jwt;
        }
    }

}
