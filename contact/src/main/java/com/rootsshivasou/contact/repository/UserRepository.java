package com.rootsshivasou.contact.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rootsshivasou.moduleCommun.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    // Méthode pour rechercher un utilisateur par son pseudonyme
    User findByNickname(String nickname);
    
}