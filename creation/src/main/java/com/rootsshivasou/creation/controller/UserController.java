package com.rootsshivasou.creation.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rootsshivasou.creation.repository.UserRepository;
import com.rootsshivasou.creation.service.UserService;
import com.rootsshivasou.creation.service.email.TokenInvalidException;
import com.rootsshivasou.moduleCommun.enums.Role;
import com.rootsshivasou.moduleCommun.model.Post;
import com.rootsshivasou.moduleCommun.model.User;
import com.rootsshivasou.moduleCommun.model.dto.UserDTO;

@RestController
@CrossOrigin(origins = "*") // URL qui pourront passer une requête
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService service;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public List<UserDTO> all() {
        List<UserDTO> list = new ArrayList<UserDTO>();
        for (User user : service.getAllUsers()) {
            list.add(new UserDTO(user));
        }
        return list;
    }

    @GetMapping("/user/{id}")
    public UserDTO user(@PathVariable("id") int identifier) {
        return new UserDTO(service.getUser(identifier));
    }

    /**
     * L'annotation requestbody permet de récurerer les infos écrits dans le corps
     * 
     * @param u
     * @return
     */
    @PostMapping("/user")
    public ResponseEntity<?> insertUser(@RequestBody User u) {

        // Vérifier si l'email ou le nom d'utilisateur existe déjà
    if (service.existsByEmail(u.getEmail())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : L'email est déjà utilisé.");
    }
    
    if (service.existsByNickname(u.getNickname())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : Le nom d'utilisateur est déjà pris.");
    }
        
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        User savedUser = service.saveUser(u);

        // Appel de la méthode pour envoyer l'e-mail de vérification
        service.registerUser(savedUser);

        // Mettre à jour l'utilisateur avec le token de vérification et ensuite vérifier le compte par e-mail

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(savedUser));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String verificationToken) {
        try {
            // log de vérification du token
            logger.info("Verification attempt with token: {}", verificationToken);

            // Appelle de la méthode de service pour vérifier le compte
            service.verifyAccount(verificationToken);

            // Si la vérification est réussie, redirigez l'utilisateur ou afficher un
            // message de succès
            logger.info("Verification successful for token: {}", verificationToken);
            return ResponseEntity.status(HttpStatus.OK).body("Compte vérifié. Vous pouvez desormais vous connecter.");
        } catch (TokenInvalidException e) {
            // Si la vérification échoue en raison d'un jeton non valide, renvoyer un
            // message d'erreur approprié
            logger.error("Verification failed. Invalid token: {}", verificationToken, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La vérification du compte a échoué. Veuillez vérifier votre lien.");
        }

    }

    @DeleteMapping("/user/{id}")
    public Boolean deleteUser(@PathVariable("id") int id) {
        User u = service.getUser(id);
        if (u != null) {
            service.deleteUser(id);
            return true;
        } else {
            return false;
        }
    }

    @PutMapping("/user/{id}")
    public UserDTO updateUser(@PathVariable("id") int userId, @RequestBody UserDTO updatedUserDTO) {
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
            if (updatedUserDTO.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(updatedUserDTO.getPassword()));
            }
            
            if (updatedUserDTO.getRoles() != null) {
                Set<Role> newRoles = updatedUserDTO.getRoles().stream()
                    .map(roleString -> Role.valueOf(roleString.toUpperCase()))
                    .collect(Collectors.toSet());
                existingUser.setRoles(newRoles);
            }
    
            // Enregistrer l'utilisateur mis à jour dans la base de données
            userRepository.save(existingUser);
    
            // Retournez le DTO mis à jour
            return new UserDTO(existingUser);
        } else {
            // L'utilisateur avec l'ID spécifié n'a pas été trouvé
            return null;
        }
    }
    

    // Ajouter un post à un utilisateur
    @PostMapping("/user/{userId}/posts")
    public UserDTO addPostToUser(@PathVariable("userId") int userId, @RequestBody Post post) {
        User user = service.getUser(userId);
        List<Post> posts = user.getPosts();
        posts.add(post);
        service.saveUser(user);
        return new UserDTO(user);
    }

    // Supprimer un post d'un utilisateur
    @DeleteMapping("/user/{userId}/posts/{postId}")
    public UserDTO removePostFromUser(@PathVariable("userId") int userId, @PathVariable("postId") int postId) {
        User user = service.getUser(userId);
        List<Post> posts = user.getPosts();

        // Supprimer le post de la liste
        posts.removeIf(p -> p.getId() == postId);

        service.saveUser(user);
        return new UserDTO(user);
    }

}