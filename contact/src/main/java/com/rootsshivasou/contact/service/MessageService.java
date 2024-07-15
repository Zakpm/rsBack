package com.rootsshivasou.contact.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rootsshivasou.contact.repository.MessageRepository;
import com.rootsshivasou.moduleCommun.model.Message;


@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
   

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }

    public Message getMessage(int id) {
        Optional<Message> optional = messageRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * utilisée pour les creates
     * 
     * @param m
     * @return
     */
    
     public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public void deletMessage(int id) {
        messageRepository.deleteById(id);
        ;
    }

    // Méthode pour récupérer le dernier message envoyé par un utilisateur donné
    public Message getLastMessageByUserId(Integer userId) {
        // Récupérer la liste des messages envoyés par l'utilisateur, triée par date de création décroissante
        List<Message> userMessages = messageRepository.findByUserIdOrderByCreatedAtDesc(userId);
        // Vérifier si des messages ont été trouvés
        if (!userMessages.isEmpty()) {
            // Retourner le premier message (le plus récent) de la liste
            return userMessages.get(0);
        } else {
            // Aucun message trouvé, retourner null
            return null;
        }
    }
    

}