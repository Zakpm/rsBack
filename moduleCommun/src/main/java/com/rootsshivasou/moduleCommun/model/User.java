package com.rootsshivasou.moduleCommun.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.rootsshivasou.moduleCommun.enums.Genre;
import com.rootsshivasou.moduleCommun.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Email(message = "L'adresse mail doit être valide.")
    @NotBlank(message = "L'email est obligatoire.")
    @Size(max = 180, message = "L'email doit faire 180 caractères max.")
    @Column(length = 180, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le pseudo est obligatoire.")
    @Size(min = 3, max = 30, message = "Le pseudo doit faire entre 3 et 30 caractères.")
    @Column(length = 30, nullable = false, unique = true)
    private String nickname;

    @NotBlank(message = "Le mot de passe est obligatoire.")
    @Size(min = 12, max = 4000, message = "le mot de passe doît faire entre 12 et 4000 caractères." )
    @Column(length = 4000,nullable = false)
    @Pattern(
        regexp = "^(?=.*[a-zà-ÿ])(?=.*[A-ZÀ-Ỳ])(?=.*[0-9])(?=.*[^a-zà-ÿA-ZÀ-Ỳ0-9]).{12,}$",
        message = "Le mot de passe doit contenir au moins une lettre minuscule, une lettre majuscule, un chiffre et un caractère spécial.")
    private String password;

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(max = 255, message = "le nom doît faire 255 caractères maximum." )
    @Column(length = 255, nullable = false)
    private String last_name;

    @NotBlank(message = "Le prénom est obligatoire.")
    @Size(max = 255, message = "le prénom doît faire 255 caractères maximum." )
    @Column(length = 255, nullable = false)
    private String first_name;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean is_verified = false;

    @Column(name = "token", nullable = true)
    private String token;

    @Column(name = "token_for_email_verification",nullable = true)
    private String tokenForEmailVerification;

    @Column(nullable = true)
    private LocalDateTime created_at;

    // cette méthode va sera appelée avant la création d'un user et au moment de l'enregistrement la date actuelle sera enregistrée
    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }

    @PostPersist
    @PreUpdate
    protected void onCreateOrUpdate() {
    if (is_verified) {
        verified_at = LocalDateTime.now();
    }
}

    @Column(nullable = true)
    private LocalDateTime verified_at;

    @Column(nullable = true)
    private LocalDateTime updated_at;

    @Column(nullable = true)
    private LocalDateTime expires_at;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le genre est obligatoire.")
    @Column(nullable = false)
    private Genre genre;

    @NotNull(message = "La date de naissance est obligatoire.")
    @Column(nullable = false)
    private LocalDate date_naissance;

    @ElementCollection
    @Column(nullable = false)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles = new HashSet<>(Collections.singletonList(Role.ROLE_USER));

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Post> posts;

   @OneToMany(mappedBy = "userRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResetPasswordRequest> requests = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Message> messages;
   
    
}