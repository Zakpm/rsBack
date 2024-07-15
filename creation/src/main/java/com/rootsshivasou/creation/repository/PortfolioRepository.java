package com.rootsshivasou.creation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rootsshivasou.moduleCommun.model.Portfolio;

@Repository
public interface PortfolioRepository extends CrudRepository<Portfolio, Integer> {

}