package com.rootsshivasou.moduleCommun.model.dto;


import java.util.List;

import com.rootsshivasou.moduleCommun.model.Portfolio;

import lombok.Data;

@Data
public class PortfolioDTO {

    private Integer id;
    private List<String> image;

    public PortfolioDTO () {

    }

    public PortfolioDTO (Portfolio p) {
        this.id = p.getId();
        this.image = p.getImage();

    }
    
}