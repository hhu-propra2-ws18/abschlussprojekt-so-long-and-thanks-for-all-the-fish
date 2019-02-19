package de.hhu.ProPra.conflict.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.RequestMapping;
import de.hhu.ProPra.conflict.service.MailService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    private MailService mailService;

    @RequestMapping("/")
    public String tester(){
        System.out.println("lol");
        return "Test";
    }

    @RequestMapping("/sendEmail/{id}")
    public String send() {
        String conflictMessage = "";
        long id = 1;
        try{
            mailService.sendTest(id, conflictMessage);
            System.out.println("Should be sent");
        } catch(MailException e){
            System.out.println("error");
            //catch error
        }

        return "test";
    }
}
