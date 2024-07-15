package com.rootsshivasou.moduleCommun.model;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Entity
public class Post {
    
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
        updated_at = LocalDateTime.now();
    }

    @Column(nullable = true, length = 10000)
    private List<String> image = new ArrayList<>();

    @Column(nullable = true, length = 5000)
    private String video;

    @Lob
    @NotBlank(message = "Le contenu est obligatoire.")
    @Column(nullable = false, columnDefinition="LONGTEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean is_published = false;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean is_featured = false;

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

    @Column(nullable = true)
    private LocalDateTime published_at;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    

    public void setIs_published(Boolean is_published) {
        // Si is_published est true, définir published_at à la date et heure actuelles
        if (Boolean.TRUE.equals(is_published)) {
            this.published_at = LocalDateTime.now();
        }
        // Si is_published est false ou null, effacer la date de publication
        else {
            this.published_at = null;
        }
    
        this.is_published = is_published;
    }
    
    
    
}