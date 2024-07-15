package com.rootsshivasou.creation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rootsshivasou.moduleCommun.model.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

}