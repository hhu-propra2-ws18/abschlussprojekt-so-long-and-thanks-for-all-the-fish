package de.hhu.ProPra.conflict.controller;


import de.hhu.ProPra.conflict.model.Lending;
import de.hhu.ProPra.conflict.model.User;
import de.hhu.ProPra.conflict.service.Repositorys.LendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import de.hhu.ProPra.conflict.service.MailService;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    LendingRepository lendRepo;

    public String send(long lendId,String conflictMessage,long ownerId,long lenderId) {
        try{
            mailService.sendTest(lendId, conflictMessage,ownerId,lenderId);
            System.out.println("Should be sent");
        } catch(MailException e){
            System.out.println("error");
            //catch error
        }

        return "test";
    }

    @GetMapping("/openConflict")
    public String openConflict(Model model){
        return "conflictUserOpen";
    }

    @PostMapping("/openConflict")
    public String openConflictpost(Model model, @RequestParam(value = "action") String button,@RequestParam long lendingID,
    @RequestParam String description){
        if(button.equals("back")){
            return "Back";
        }
        if(button.equals("open")){
            System.out.println("open");
            Lending l = lendRepo.findByLendingID(lendingID);
            System.out.println(l==null);
            System.out.println(lendRepo.count());
            l.setConflict(true);
            l.getLendingPerson();
            User owner = l.getLendedArticle().getOwnerUser();
            send(lendingID,description,(owner.getId()),(l.getLendingPerson().getId()));

        }
        return "conflict submitted";

    }
}
