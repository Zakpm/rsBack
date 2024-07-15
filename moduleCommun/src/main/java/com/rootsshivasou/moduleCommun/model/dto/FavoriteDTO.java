package com.rootsshivasou.moduleCommun.model.dto;

import java.time.LocalDateTime;

import com.rootsshivasou.moduleCommun.model.Favorite;


import lombok.Data;

@Data
public class FavoriteDTO {

    private Integer id;
    private Boolean favorite;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer userId;
    private Integer postId;

    public FavoriteDTO() {
    }

    public FavoriteDTO(Favorite f) {
        this.id = f.getId();
        this.favorite = f.getFavorite();
        this.createdAt = f.getCreatedAt();
        this.updatedAt = f.getUpdatedAt();
        this.userId = f.getUser().getId();
        this.postId = f.getPost().getId();
    }

}