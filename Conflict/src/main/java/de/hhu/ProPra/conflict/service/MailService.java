package de.hhu.ProPra.conflict.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;
    }

    public void sendTest() throws MailException {
        //sendEmail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo("rokra102@hhu.de");
        mail.setFrom("rhinoshareconflict@gmail.com");
        mail.setSubject("Nein");
        mail.setText("Staubsauger nicht gut. Geld zurück bitte");

        javaMailSender.send(mail);
    }
}
