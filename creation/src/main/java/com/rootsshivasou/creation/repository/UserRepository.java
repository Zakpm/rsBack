package com.rootsshivasou.creation.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rootsshivasou.moduleCommun.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByTokenForEmailVerification(String tokenForEmailVerification);
    User findByToken(String token);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);
    
}