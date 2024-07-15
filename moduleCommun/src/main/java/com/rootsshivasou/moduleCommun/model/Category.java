package com.rootsshivasou.moduleCommun.model;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer id;

    @NotBlank(message = "Le titre est obligatoire.")
    @Size(max = 255, message = "Le titre doit faire 255 caractères maximum.")
    @Column(nullable = false)
    private String title;

    private String slug;

    private String generateSlug(String input) {
        // Normaliser en Unicode NFD et supprimer les caractères diacritiques
    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    String slug = pattern.matcher(normalized).replaceAll("");

    // Convertir en minuscules, remplacer les caractères non alphabétiques ou non numériques par des tirets
    slug = slug.toLowerCase().replaceAll("[^a-z0-9]+", "-");

    // Supprimer les tirets au début et à la fin
    return slug.replaceAll("^-|-$", "");
    }

    // cette propriété sert à générer le slug 
    @PreUpdate
    private void onPrepersist() {
        this.slug = generateSlug(this.title);
    }

    @Column(nullable = true)
    private LocalDateTime created_at;

    // cette méthode va sera appelée avant la création d'un user et au moment de l'enregistrement la date actuelle sera enregistrée
    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        this.slug = generateSlug(this.title);
    }

    @Column(nullable = true)
    private LocalDateTime updated_at;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Post> posts;

    
    
}