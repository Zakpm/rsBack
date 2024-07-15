package com.rootsshivasou.contact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.rootsshivasou.contact.service.MessageService;
import com.rootsshivasou.contact.service.UserService;
import com.rootsshivasou.moduleCommun.model.Message;

public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Traitement des messages entrants
        String payload = message.getPayload();

        // Identifiant de l'utilisateur actuellement connecté
        Integer userId = (Integer) session.getAttributes().get("userId");

        // Vérifiez si le message est destiné à l'administrateur
        if (userId != 1) {
            // Enregistrez le message dans votre système
            Message newMessage = new Message();
            newMessage.setContent(payload);
            newMessage.setUser(userService.getUserById(userId));
            messageService.saveMessage(newMessage);
        } else {
            // Récupérer le dernier message envoyé par l'utilisateur à l'administrateur
            Message lastMessage = messageService.getLastMessageByUserId(userId);

            // Vérifier si un message existe
            if (lastMessage != null) {
                // Récupérer l'identifiant de l'utilisateur qui a envoyé le dernier message
                Integer recipientId = lastMessage.getUser().getId();

                // Créer un nouveau message de l'administrateur destiné à cet utilisateur
                Message adminMessage = new Message();
                adminMessage.setContent(payload);
                adminMessage.setUser(userService.getAdminUser()); // Récupérer l'utilisateur administrateur
                adminMessage.setRecipientId(recipientId); // Définir l'utilisateur destinataire
                messageService.saveMessage(adminMessage); // Enregistrer le nouveau message
            }
        }
    }

}
