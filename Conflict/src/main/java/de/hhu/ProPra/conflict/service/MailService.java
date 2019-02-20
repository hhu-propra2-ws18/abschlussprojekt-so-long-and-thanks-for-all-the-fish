package de.hhu.ProPra.conflict.service;

import de.hhu.ProPra.conflict.model.User;
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

    public void sendTest(long lendingId, String conflictMessage, long ownerId, long lenderId, User admin) throws MailException {
        //sendEmail
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(admin.getEmail());
        mail.setFrom("rhinoshareconflict@gmail.com");
        mail.setSubject("Conflict" + lendingId);
        mail.setText("LendingId: " + lendingId + "\nConflict message: " + conflictMessage + "\n" + "\nOwnerID:" + ownerId + "\nlenderID:" + lenderId + "");

        javaMailSender.send(mail);
    }
}