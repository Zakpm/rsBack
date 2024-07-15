package com.rootsshivasou.security.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rootsshivasou.moduleCommun.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    User findByNickname(String nickname);

    // Recherche un utilisateur par e-mail ou pseudo
    @Query("SELECT u FROM User u WHERE u.email = :emailOrNickname OR u.nickname = :emailOrNickname")
    User findByEmailOrNickname(@Param("emailOrNickname") String emailOrNickname);

}