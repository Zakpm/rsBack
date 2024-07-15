package com.rootsshivasou.creation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rootsshivasou.moduleCommun.model.Favorite;

@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, Integer> {

    List<Favorite> findByUserId(Integer userId);
    
    Optional<Favorite> findByIdAndUserId(Integer id, Integer userId);

    
} 