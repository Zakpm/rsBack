package com.rootsshivasou.security.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Envoie un e-mail.
     * 
     * @param to      L'adresse e-mail du destinataire.
     * @param subject Le sujet de l'e-mail.
     * @param content Le contenu de l'e-mail.
     */
    public void sendEmail(String to, String subject, String resetLink) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            // Construction du contenu HTML
            String content = "<p>Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien ci-dessous :</p>" +
            "<a href='" + resetLink + "'>Cliquez ici pour réinitialiser votre mot de passe</a>";

            helper.setText(content, true); // true pour activer le contenu HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Gérer l'exception d'envoi d'e-mail
            e.printStackTrace();
        }
    }
}
