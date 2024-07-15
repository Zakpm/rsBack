package com.rootsshivasou.creation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootsshivasou.creation.repository.FavoriteRepository;
import com.rootsshivasou.creation.service.exception.FavoriteNotFoundException;
import com.rootsshivasou.moduleCommun.model.Favorite;
import com.rootsshivasou.moduleCommun.model.User;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getAllFavs() {
        return (List<Favorite>) favoriteRepository.findAll();
    }

    public Favorite getFav(int id) {
        Optional<Favorite> optional = favoriteRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * méthode qui sera utilisée pour les inserts et les updates
     * 
     * @param u
     * @return
     */
    public Favorite saveFav(Favorite f, Integer userId) {
        // Créer un objet User à partir de l'ID de l'utilisateur
        User user = new User();
        user.setId(userId);
        // Assurez-vous que l'utilisateur associé est défini correctement
        f.setUser(user);
        return favoriteRepository.save(f);
    }

    public void deleteFav(int id, Integer userId) {
        // Rechercher le favori en fonction de son ID et de l'ID de l'utilisateur associé
        Optional<Favorite> optionalFavorite = favoriteRepository.findByIdAndUserId(id, userId);
        
        // Vérifier si le favori existe
        if (optionalFavorite.isPresent()) {
            // Si le favori existe, le supprimer
            favoriteRepository.deleteById(id);
        } else {
            // Sinon, gérer l'erreur ou envoyer un message indiquant que le favori n'a pas été trouvé
            // Ici, vous pouvez jeter une exception, renvoyer un code d'erreur, ou prendre une autre action selon vos besoins
            // Par exemple :
            throw new FavoriteNotFoundException("Favorite not found with id " + id + " for user " + userId);
        }
    }
    


    public List<Favorite> getFavoritesByUserId(Integer userId) {
        return favoriteRepository.findByUserId(userId);
    }

}