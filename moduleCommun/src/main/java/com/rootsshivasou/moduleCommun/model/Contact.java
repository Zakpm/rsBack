package com.rootsshivasou.moduleCommun.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le prénom est obligatoire.")
    @Size(max = 255, message = "le prénom doît faire 255 caractères maximum." )
    @Column(length = 255, nullable = false)
    private String first_name;

    @NotBlank(message = "Le nom est obligatoire.")
    @Size(max = 255, message = "le nom doît faire 255 caractères maximum." )
    @Column(length = 255, nullable = false)
    private String last_name;

    @Email(message = "L'adresse email doit être valide.")
    @NotBlank(message = "L'email est obligatoire.")
    @Size(max = 180, message = "L'email doit faire 180 caractères max.")
    @Column(length = 180, nullable = false)
    private String email;

    @Column(length = 180, nullable = true)
    @Pattern(
        regexp = "^[0-9\\-\\+\\s\\(\\)]{6,30}$",
        message = "Veuillez entrer un numéro de téléphone valide."
    )
    private String phone;

    @NotBlank(message = "L'objet est obligatoire.")
    @Size(max = 30, message = "l'objet doît faire 30 caractères maximum." )
    @Column(length = 30, nullable = false)
    private String object;

    @Lob
    // lob c'est pour les textes longs 
    @NotBlank(message = "Le contenu est obligatoire.")
    @Column(nullable = false, length = 1000, columnDefinition="TEXT")
    @Size(max = 1000, message = "La longueur du contenu ne peut pas dépasser 1000 caractères.")
    private String content;

    @Column(nullable = true)
    private LocalDateTime created_at;
    
    // cette méthode va sera appelée avant la création d'un user et au moment de l'enregistrement la date actuelle sera enregistrée
    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }



    
}