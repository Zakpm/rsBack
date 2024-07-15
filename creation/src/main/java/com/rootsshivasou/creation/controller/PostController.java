package com.rootsshivasou.creation.controller;

import com.rootsshivasou.creation.service.CategoryService;
import com.rootsshivasou.creation.service.PostService;
import com.rootsshivasou.creation.service.UserService;
import com.rootsshivasou.moduleCommun.model.Category;
import com.rootsshivasou.moduleCommun.model.Post;
import com.rootsshivasou.moduleCommun.model.User;
import com.rootsshivasou.moduleCommun.model.dto.PostDTO;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*") // URL qui pourront passer une requête
public class PostController {

    @Autowired
    PostService service;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;
    

    @GetMapping("/posts")
    public List<PostDTO> all() {
        List<PostDTO> list = new ArrayList<PostDTO>();
        for (Post post : service.getAllPosts()) {
            list.add(new PostDTO(post));
        }
        return list;
    }

    @GetMapping("/post/{id}")
    public PostDTO post(@PathVariable("id") int identifier) {
        return new PostDTO(service.getPost(identifier));
    }

    /**
     * L'annotation requestbody permet de récurerer les infos écrits dans le corps
     *
     * @param u
     * @return
     */
    @PostMapping("/post")
    public ResponseEntity<PostDTO> insertPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("video") String video,
            @RequestParam("userId") Integer userId, // a enlever
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(value = "isPublished", required = false, defaultValue = "false") Boolean isPublished,
            @RequestParam(value = "isFeatured", required = false, defaultValue = "false") Boolean isFeatured,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        try {

            // Récupérer la catégorie et l'utilisateur associés
            Category category = categoryService.getCategory(categoryId);
            User user = userService.getUser(userId);
            if (category == null || user == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // Créez un nouvel objet Post
            Post newPost = new Post();
            newPost.setTitle(title);
            newPost.setContent(content);
            newPost.setVideo(video);
            newPost.setCategory(category);
            newPost.setUser(user);
            newPost.setIs_published(isPublished);
            newPost.setIs_featured(isFeatured);

            // Traiter les images
            if (images != null && !images.isEmpty()) {
                List<String> imagePaths = storeImages(images);
                newPost.setImage(imagePaths); // s'assurer que Post a une propriété images
            }

            // Sauvegardez le post
            Post savedPost = service.savePost(newPost);

            // Convertire l'entité Post sauvegardée en DTO pour la réponse
            PostDTO savedPostDTO = new PostDTO(savedPost);

            // Renvoyer la réponse avec le DTO du post créé
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPostDTO);
        } catch (Exception e) {
            // Gérer les exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private List<String> storeImages(List<MultipartFile> images) throws IOException {
        List<String> imagePaths = new ArrayList<>();

        // Dossier où les images seront stockées
        String uploadDir = "images/";
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
                imagePaths.add("/images/" + fileName);
            }
        }

        // Retourner la liste des chemins d'images
        return imagePaths;
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") int id) {
        Post post = service.getPost(id);
        if (post != null) {
            // Supprimer les images associées
            if (post.getImage() != null) {
                for (String imagePath : post.getImage()) {
                    deleteImage(imagePath);
                }
            }

            // Supprimer le post
            service.deletePost(id);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Méthode pour supprimer l'image
    private void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Path path = Paths.get(imagePath.replace("/images/", "images/"));
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // Gérer l'exception
                e.printStackTrace();
            }
        }
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable("id") int id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("video") String video,
            @RequestParam(value = "isPublished", required = false, defaultValue = "false") Boolean isPublished,
            @RequestParam(value = "isFeatured", required = false, defaultValue = "false") Boolean isFeatured,
            @RequestPart(value = "image", required = false) List<MultipartFile> images) {
        try {
            // Récupérer le post existant
            Post existingPost = service.getPost(id);

            // Vérifier si le post existe
            if (existingPost == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Mettre à jour les champs du post
            existingPost.setTitle(title);
            existingPost.setContent(content);
            existingPost.setVideo(video);
            existingPost.setIs_published(isPublished);
            existingPost.setIs_featured(isFeatured);

            // Traiter les nouvelles images
            if (images != null && !images.isEmpty()) {
                // Supprimer les anciennes images si nécessaire
                if (existingPost.getImage() != null) {
                    for (String imagePath : existingPost.getImage()) {
                        deleteImage(imagePath);
                    }
                }

                // Stocker les nouvelles images et mettre à jour le post
                List<String> imagePaths = storeImages(images);
                existingPost.setImage(imagePaths);
            }

            // Sauvegarder le post mis à jour
            Post updatedPost = service.savePost(existingPost);

            // Convertire l'entité Post sauvegardée en DTO pour la réponse
            PostDTO updatedPostDTO = new PostDTO(updatedPost);

            // Renvoyer la réponse avec le DTO du post mis à jour
            return ResponseEntity.status(HttpStatus.OK).body(updatedPostDTO);
        } catch (Exception e) {
            // Gérer les exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) {
        try {
            System.out.println("Received image upload request");
            
            if (image == null) {
                System.out.println("No image data received");
                return ResponseEntity.badRequest().body("No image data received");
            }
    
            System.out.println("Received image: " + image.getOriginalFilename() + ", Size: " + image.getSize());
    
            // Traiter l'image et la stocker sur le serveur en utilisant la méthode existante
            List<String> imagePaths = storeImages(Collections.singletonList(image));
    
            // Récupérer l'URL de base à partir de la requête HTTP
            String baseUrl = request.getRequestURL().toString();
            String basePath = baseUrl.substring(0, baseUrl.length() - request.getRequestURI().length() + request.getContextPath().length());
    
            // Construire le lien complet pour l'image téléchargée
            String fullImagePath = basePath + imagePaths.get(0);
    
            // Vérifier si l'image a été correctement stockée et renvoyer l'URL de l'image téléchargée
            if (imagePaths != null && !imagePaths.isEmpty()) {
                System.out.println("Image uploaded successfully");
                return ResponseEntity.ok(fullImagePath); // Retourner le lien complet de l'image téléchargée
            } else {
                System.out.println("Failed to upload image");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
            }
        } catch (IOException e) {
            // Gérer les erreurs de traitement d'image
            System.out.println("Error processing image upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image upload");
        }
    }



    



    @GetMapping("/post/user/{userId}")
    public String user(@PathVariable("userId") int id) {
        Post post = service.getPost(id);
        if (post != null) {
            return post.getUser().getNickname();
        }
        return null;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}