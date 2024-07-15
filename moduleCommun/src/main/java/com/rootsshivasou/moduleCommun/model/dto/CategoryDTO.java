package com.rootsshivasou.moduleCommun.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.rootsshivasou.moduleCommun.model.Category;
import com.rootsshivasou.moduleCommun.model.Post;

import lombok.Data;

@Data
public class CategoryDTO {

    private Integer id;
    private String title;
    private String slug;
    private List<Integer> posts;

    public CategoryDTO () {
        
    }

    public CategoryDTO (Category c) {
        this.id = c.getId();
        this.title = c.getTitle();
        this.slug = c.getSlug();
        this.posts = new ArrayList<>();
        if (c.getPosts() != null) {
            for (Post p : c.getPosts()) {
                this.posts.add(p.getId());
            }
        }

    }
    
    
}