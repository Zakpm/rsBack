package com.rootsshivasou.creation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootsshivasou.creation.repository.PortfolioRepository;
import com.rootsshivasou.moduleCommun.model.Portfolio;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public List<Portfolio> getAllPortfolios() {
        return (List<Portfolio>) portfolioRepository.findAll();
    }

    public Portfolio getPortfolio(int id) {
        Optional<Portfolio> optional = portfolioRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * méthode qui sera utilisée pour les inserts et les updates
     * @param u
     * @return
     */
    public Portfolio savePortfolio(Portfolio p) {
        return portfolioRepository.save(p);
    }

    
    public void deletePortfolio(int id) {
        portfolioRepository.deleteById(id);
    }
    
}