package com.rootsshivasou.moduleCommun.model.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.rootsshivasou.moduleCommun.model.Post;

import lombok.Data;

@Data
public class PostDTO {

    private Integer id;
    private String title;
    private String slug;
    private List<String> image;
    private String video;
    private String content;
    private Integer userId;
    private Integer categoryId;
    private Boolean isPublished;
    private Boolean isFeatured;
    private LocalDateTime publishedAt;

    public PostDTO () {
        
    }

    public PostDTO(Post p) {

        this.id = p.getId();
        this.title = p.getTitle();
        this.slug = p.getSlug();
        this.image = p.getImage();
        this.video = p.getVideo();
        this.content = p.getContent();
        if (p.getUser() != null) {
            this.userId = p.getUser().getId();
        }
        if (p.getCategory() != null) {
            this.categoryId = p.getCategory().getId();
        }
        this.isPublished = p.getIs_published();
        this.isFeatured = p.getIs_featured();
        this.publishedAt = p.getPublished_at();
        
    }
    
}