package de.hhu.rhinoshareapp.domain.mail;


import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    UserRepository userRepo;

    public void sendConflict(long lendingId, String conflictMessage, long ownerId, long lenderId) throws MailException {
        List<Person> admins = userRepo.findByRole("ROLE_ADMIN");
        for(int i=0; i<admins.size();i++){
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(admins.get(i).getEmail());
            mail.setFrom("rhinoshareconflict@gmail.com");
            mail.setSubject("Conflict" + lendingId);
            mail.setText("LendingId: " + lendingId + "\nConflict message: " + conflictMessage + "\n" + "\nOwnerID:" + ownerId + "\nlenderID:" + lenderId + "");
            javaMailSender.send(mail);
        }
    }
}
