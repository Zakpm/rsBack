package com.rootsshivasou.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rootsshivasou.moduleCommun.model.User;
import com.rootsshivasou.security.controller.JwtTokenProvider;
import com.rootsshivasou.security.repository.UserRepository; // Et un UserRepository
import java.util.stream.Collectors;
import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider; 

    public String authenticate(String login, String password) {
        User user = userRepository.findByEmailOrNickname(login); 

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            if (!user.getIs_verified()) {
                throw new RuntimeException("Compte non vérifié");
            }
            List<String> roles = user.getRoles().stream()
                                     .map(Enum::name)
                                     .collect(Collectors.toList());         
            String firstName = user.getFirst_name();
            String lastName = user.getLast_name();
            String email = user.getEmail();
            Integer id = user.getId();

            return jwtTokenProvider.createToken(login, roles, firstName, lastName, email, id);
        } else {
            throw new RuntimeException("Informations de connexion invalides");
        }
    }
}


