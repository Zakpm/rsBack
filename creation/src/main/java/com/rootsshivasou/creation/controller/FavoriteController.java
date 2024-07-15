package com.rootsshivasou.creation.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rootsshivasou.creation.service.FavoriteService;
import com.rootsshivasou.moduleCommun.model.Favorite;
import com.rootsshivasou.moduleCommun.model.dto.FavoriteDTO;

@RestController
@CrossOrigin(origins = "*") // URL qui pourront passer une requête
public class FavoriteController {

    @Autowired
    FavoriteService service;

    @GetMapping("/favs")
    public List<FavoriteDTO> all() {
        List<FavoriteDTO> list = new ArrayList<FavoriteDTO>();
        for (Favorite fav : service.getAllFavs()) {
            list.add(new FavoriteDTO(fav));
        }
        return list;
    }

    @GetMapping("/fav/{id}")
    public FavoriteDTO post(@PathVariable("id") int identifier) {
        return new FavoriteDTO(service.getFav(identifier));
    }

    /**
     * L'annotation requestbody permet de récurerer les infos écrits dans le corps
     * 
     * @param u
     * @return
     */
    @PostMapping("/fav")
    public FavoriteDTO insertFav(@RequestBody Favorite f) {
        return new FavoriteDTO(service.saveFav(f, f.getUser().getId()));
    }

    @DeleteMapping("/fav/{id}")
    public Boolean deleteFav(@PathVariable("id") int id, @RequestBody Favorite f) {
        Integer userId = f.getUser().getId();
        service.deleteFav(id, userId);
        return true;
    }

    @GetMapping("/user/fav/{id}")
    public List<FavoriteDTO> getFavByUserId(@PathVariable("id") int id) {
        List<FavoriteDTO> list = new ArrayList<FavoriteDTO>();
        for (Favorite fav : service.getFavoritesByUserId(id)) {
            list.add(new FavoriteDTO(fav));
        }
        return list;
    }

}