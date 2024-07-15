package com.rootsshivasou.creation.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rootsshivasou.creation.service.PortfolioService;
import com.rootsshivasou.moduleCommun.model.Portfolio;
import com.rootsshivasou.moduleCommun.model.dto.PortfolioDTO;

@RestController
@CrossOrigin(origins = "*") // URL qui pourront passer une requête
public class PortfolioController {

    @Autowired
    private PortfolioService service;

   

    @GetMapping("/portfolios")
    public List<PortfolioDTO> all() {
        List<PortfolioDTO> list = new ArrayList<PortfolioDTO>();
        for (Portfolio portfolio : service.getAllPortfolios()) {
            list.add(new PortfolioDTO(portfolio));
        }
        return list;
    }

    @GetMapping("/portfolio/{id}")
    public PortfolioDTO post(@PathVariable("id") int identifier) {
        return new PortfolioDTO(service.getPortfolio(identifier));
    }

    @PostMapping("/portfolio")
public ResponseEntity<PortfolioDTO> insertPortfolio(
        @RequestPart(value = "image", required = false) List<MultipartFile> image) {
    try {

        // Créer un nouvel objet portfolio
        Portfolio newPortfolio = new Portfolio();

        // Traiter l'image
        if (image != null && !image.isEmpty()) {
            List<String> imagePath = storeImage(image);
            newPortfolio.setImage(imagePath); // Assurez-vous que Portfolio a une propriété image
        }

        // Sauvegarder le portfolio
        Portfolio savedPortfolio = service.savePortfolio(newPortfolio);

        // Convertir l'entité Portfolio sauvegardée en DTO pour la réponse
        PortfolioDTO savedPortfolioDTO = new PortfolioDTO(savedPortfolio);

        // Renvoyer la réponse avec le DTO du portfolio créé
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPortfolioDTO);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

private List<String> storeImage(List<MultipartFile> images) throws IOException {
    List<String> imagePaths = new ArrayList<>();

    // Dossier où les images seront stockées
    String uploadDir = "images/portfolio/";
    Path uploadPath = Paths.get(uploadDir);

    // Créer le dossier s'il n'existe pas
    if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
    }

    for (MultipartFile image : images) {
        if (image != null && !image.isEmpty()) {
            // Générer un nom de fichier unique
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

            // Chemin complet du fichier
            Path filePath = uploadPath.resolve(fileName);

            // Sauvegarder l'image dans le dossier
            Files.copy(image.getInputStream(), filePath);

            // Ajouter le chemin de l'image à la liste
            imagePaths.add("/images/portfolio/" + fileName);
        }
    }

    // Retourner la liste des chemins d'images
    return imagePaths;
}


@DeleteMapping("/portfolio/{id}")
public ResponseEntity<?> deletePortfolio(@PathVariable("id") int id) {

    System.out.println("Début de la suppression du portfolio d'ID " + id);

    Portfolio portfolio = service.getPortfolio(id);
    if (portfolio != null) {
        // Supprimer l'image associée
        if (portfolio.getImage() != null) {
            for (String imagePath : portfolio.getImage()) {
                deleteImage(imagePath);
            }
        }
        
        // Supprimer le portfolio
        service.deletePortfolio(id);
        
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.notFound().build();
    }
}

// Méthode pour supprimer l'image
private void deleteImage(String imagePath) {
    if (imagePath != null && !imagePath.isEmpty()) {
        Path path = Paths.get(imagePath.replace("/images/portfolio/", "images/portfolio/"));
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // Gérer l'exception
            e.printStackTrace();
        }
    }
}

}