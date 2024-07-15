package com.rootsshivasou.contact.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rootsshivasou.contact.repository.ContactRepository;
import com.rootsshivasou.moduleCommun.model.Contact;
import com.rootsshivasou.moduleCommun.model.dto.ContactDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${mail.smtp}")
    private String mailSmtp;

     public List<Contact> getAllContacts() {
        return (List<Contact>) contactRepository.findAll();
    }

    public Contact getContact(int id) {
        Optional<Contact> optional = contactRepository.findById(id);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * méthode qui sera utilisée pour les inserts et les updates
     * @param u
     * @return
     */
    public Contact saveContact(Contact c, String messageContent) {
        // Enregistre le contact dans la base de données et récupère le contact sauvegardé
        Contact savedContact = contactRepository.save(c);

        // Convertir Contact en ContactDTO
        ContactDTO contactDTO = new ContactDTO(savedContact);

         // Envoyer un e-mail avec le contenu du message
        sendHtmlEmail(contactDTO, mailSmtp, messageContent);

        // Retourner le contact sauvegardé
        return savedContact;
    }

    private void sendHtmlEmail(ContactDTO contactDTO, String subject, String messageContent) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(contactDTO.getEmail());
            helper.setSubject(subject);
            
            
            String htmlContent = "<p>" + messageContent + "</p><p>Email du contact : " + contactDTO.getEmail() + "</p>";
           helper.setText(htmlContent, true);
           javaMailSender.send(mimeMessage);


        } catch (MessagingException e) {
            // Gérer l'exception, par exemple, en journalisant l'erreur
            e.printStackTrace();
        }
    }

    public void deleteContact(int id) {
        contactRepository.deleteById(id);
    }

    

    
    
}