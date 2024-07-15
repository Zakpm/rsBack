package com.rootsshivasou.creation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootsshivasou.creation.repository.PostRepository;
import com.rootsshivasou.moduleCommun.model.Post;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

     public List<Post> getAllPosts() {
        return (List<Post>) postRepository.findAll();
    }

    public Post getPost(int id) {
        Optional<Post> optional = postRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * méthode qui sera utilisée pour les inserts et les updates
     * @param u
     * @return
     */
    public Post savePost(Post p) {
        return postRepository.save(p);
    }

    public void deletePost(int id) {
        postRepository.deleteById(id);
    }
    
}