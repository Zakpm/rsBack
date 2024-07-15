package com.rootsshivasou.creation.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rootsshivasou.creation.repository.UserRepository;
import com.rootsshivasou.creation.service.email.EmailService;
import com.rootsshivasou.creation.service.email.TokenInvalidException;
import com.rootsshivasou.moduleCommun.enums.Role;
import com.rootsshivasou.moduleCommun.model.User;
import com.rootsshivasou.moduleCommun.model.dto.UserDTO;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Value("${app.base.url}") //la base URL de l' application dans le fichier de configuration
    private String baseUrl;

    
    
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUser(int id) {
        Optional<User> optional = userRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * méthode qui sera utilisée pour les inserts et les updates
     * @param u
     * @return
     */
    public User saveUser(User u) {
        return userRepository.save(u);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public UserDTO updateUser(int userId, UserDTO updatedUserDTO) {
        Optional<User> optionalExistingUser = userRepository.findById(userId);
    
        if (optionalExistingUser.isPresent()) {
            User existingUser = optionalExistingUser.get();
    
            // Mettre à jour uniquement les champs non nuls
            if (updatedUserDTO.getFirst_name() != null) {
                existingUser.setFirst_name(updatedUserDTO.getFirst_name());
            }
            if (updatedUserDTO.getLast_name() != null) {
                existingUser.setLast_name(updatedUserDTO.getLast_name());
            }
            if (updatedUserDTO.getNickname() != null) {
                existingUser.setNickname(updatedUserDTO.getNickname());
            }
            if (updatedUserDTO.getGenre() != null) {
                existingUser.setGenre(updatedUserDTO.getGenre());
            }
            
            if (updatedUserDTO.getRoles() != null) {
                Set<Role> newRoles = updatedUserDTO.getRoles().stream()
                    .map(roleString -> Role.valueOf(roleString.toUpperCase()))
                    .collect(Collectors.toSet());
                existingUser.setRoles(newRoles);
            }
            

           
    
            // Enregistrement l'utilisateur mis à jour dans la base de données
            userRepository.save(existingUser);
    
            // Retourner le DTO mis à jour
            return new UserDTO(existingUser);
        } else {
            // L'utilisateur avec l'ID spécifié n'a pas été trouvé
            return null;
        }
    }
    

    public void registerUser(User user) {
        // Génération du jeton
        String token = UUID.randomUUID().toString();

        // Envoi de l'e-mail de vérification
        String verificationLink = baseUrl + "/verify?token=" + token;
        String emailContent = " Veuillez cliquer sur le lien suivant pour vérifier votre compte : " + "<a href='"+ verificationLink + "'>Cliquez ici pour valider votre compte</a>";
        emailService.sendEmail(user.getEmail(), "Vérification du compte", emailContent);

        // Stockage du jeton
        user.setToken(token);

        // Enregistrement de l'utilisateur
        userRepository.save(user);
    }




    public boolean verifyAccount(String token) {
        // Recherche de l'utilisateur par jeton
        User user = userRepository.findByToken(token);
    
        if (user != null) {
            // Marquer l'utilisateur comme vérifié
            user.setIs_verified(true);
            user.setToken(null); // Effacer le jeton après vérification
            userRepository.save(user);
    
            // Retourner true pour indiquer que la vérification a réussi
            return true;
        } else {
            // Gérer le cas où le jeton n'est pas valide
            throw new TokenInvalidException("Le jeton de vérification n'est pas valide.");
        }
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }
   
    
    
}