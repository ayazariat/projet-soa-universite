package com.university.auth_service.service.impl;

import com.university.auth_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email.from-name:Université SOA}")
    private String fromName;

    @Override
    public void sendActivationEmail(String to, String firstName, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Activation de votre compte - Université SOA");

            String link = "http://localhost:8081/api/auth/confirm?token=" + token;
            String emailBody = String.format(
                    "Bonjour %s,\n\n" +
                            "Votre compte a été créé avec succès sur la plateforme de l'Université.\n" +
                            "Pour activer votre compte, veuillez cliquer sur le lien suivant :\n%s\n\n" +
                            "Ce lien est valable 24 heures.\n\n" +
                            "Cordialement,\n" +
                            "L'équipe de l'Université",
                    firstName, link
            );

            message.setText(emailBody);
            mailSender.send(message);
            log.info("Email d'activation envoyé avec succès à : {}", to);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email d'activation à {} : {}", to, e.getMessage(), e);
        }
    }
}
