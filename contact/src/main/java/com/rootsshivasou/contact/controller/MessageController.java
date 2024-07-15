// package com.rootsshivasou.contact.controller;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.rootsshivasou.contact.service.MessageService;
// import com.rootsshivasou.contact.service.UserService;
// import com.rootsshivasou.moduleCommun.model.Message;
// import com.rootsshivasou.moduleCommun.model.User;
// import com.rootsshivasou.moduleCommun.model.dto.MessageDTO;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;

// @RestController
// @CrossOrigin(origins = "*")
// public class MessageController {

//     @Autowired
//     private MessageService service;

//     @Autowired
//     private UserService userService;

//     @GetMapping("/messages")
//     public List<MessageDTO> All() {
//         List<MessageDTO> list = new ArrayList<MessageDTO>();
//         for (Message message : service.getAllMessages()) {
//             list.add(new MessageDTO(message));
//         }
//         return list;
//     }

//     @GetMapping("/message/{id}")
//     public MessageDTO message(@PathVariable("id") int indentifier) {
//         return new MessageDTO(service.getMessage(indentifier));
//     }

//     @PostMapping("/message")
//     public ResponseEntity<MessageDTO> insertMessage(@RequestBody MessageDTO messageDTO) {
//         // Récupère l'identifiant de l'utilisateur à partir du service
//         Integer userId = service.getFirstUserId();

//         // Vérifie si l'utilisateur existe
//         User user = userService.getUser(userId);
//         if (user == null) {
//             // Retourne une réponse indiquant que l'utilisateur n'existe pas
//             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//         }

//         // Crée un nouvel objet Message à partir du DTO
//         Message message = new Message();
//         message.setContent(messageDTO.getContent());
//         message.setUser(user);

//         // Détermine le recipientId en fonction de la logique
//         if (userId == 1) {
//             // Si l'utilisateur est l'administrateur, déterminez le recipientId en fonction
//             // de la personne à laquelle la conversation est rattachée
//             Integer recipientId = determineRecipientIdForAdmin(messageDTO);
//             message.setRecipientId(recipientId);
//         } else {
//             // Si l'utilisateur n'est pas l'administrateur, le recipientId est toujours 1
//             message.setRecipientId(1);
//         }

//         // Enregistre le message et récupère le résultat
//         Message savedMessage = service.saveMessage(messageDTO, userId);

//         // Retourne le DTO correspondant au message enregistré
//         MessageDTO savedMessageDTO = new MessageDTO(savedMessage);
//         return ResponseEntity.status(HttpStatus.CREATED).body(savedMessageDTO);
//     }

//     private Integer determineRecipientIdForAdmin(MessageDTO messageDTO) {
//         // Recherchez tous les utilisateurs qui ont envoyé un message, en excluant l'ID
//         // numéro 1 (administrateur)
//         List<Message> messageSenders = service.findAllMessageSendersExceptAdmin();

//         // Triez les utilisateurs par le moment où ils ont envoyé leur dernier message
//         // Ici, je suppose que vous avez une méthode dans votre service utilisateur pour
//         // récupérer les utilisateurs triés par la date de leur dernier message
//         List<Message> sortedUsers = service.findAllUsersSortedByLastMessageDate(messageSenders);

//         // Utilisez l'ID du premier utilisateur dans la liste triée comme destinataire
//         // du message
//         if (!sortedUsers.isEmpty()) {
//             return sortedUsers.get(0).getId(); // Retourne l'ID du premier utilisateur dans la liste triée
//         } else {
//             // Gérez le cas où aucun utilisateur n'a envoyé de message
//             return null;
//         }
//     }

//     @PutMapping("/message/{id}")
//     public ResponseEntity<MessageDTO> upMessage(@PathVariable("id") int id, @RequestBody MessageDTO upMessageDTO) {
//         MessageDTO updateMessage = service.updateMessage(id, upMessageDTO);
//         if (updateMessage != null) {
//             return new ResponseEntity<>(updateMessage, HttpStatus.OK);
//         } else {
//             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//         }
//     }

//     @DeleteMapping("/message/{id}")
//     public Boolean deleteMessage(@PathVariable("id") int id) {
//         Message m = service.getMessage(id);
//         if (m != null) {
//             service.deletMessage(id);
//             return true;
//         } else {
//             return false;
//         }
//     }

// }
