package com.rootsshivasou.creation.repository;

import org.springframework.data.repository.CrudRepository;

import com.rootsshivasou.moduleCommun.model.Post;

public interface PostRepository extends CrudRepository<Post, Integer>   {
    
}