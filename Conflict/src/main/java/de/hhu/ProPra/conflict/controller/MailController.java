package de.hhu.ProPra.conflict.controller;


import de.hhu.ProPra.conflict.model.Lending;
import de.hhu.ProPra.conflict.model.User;
import de.hhu.ProPra.conflict.service.Repositorys.LendingRepository;
import de.hhu.ProPra.conflict.service.Repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import de.hhu.ProPra.conflict.service.MailService;

import java.util.List;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    LendingRepository lendRepo;

    @Autowired
    UserRepository userRepo;

    public String send(long lendId, String conflictMessage, long ownerId, long lenderId, User admin) {
        try {
            mailService.sendTest(lendId, conflictMessage, ownerId, lenderId, admin);
        } catch (MailException e) {
            //catch error
        }

        return "test";
    }

    @GetMapping("/openConflict")
    public String openConflict(Model model) {
        return "conflictUserOpen";
    }

    @PostMapping("/openConflict")
    public String openConflictpost(Model model, @RequestParam(value = "action") String button, @RequestParam long lendingID,
                                   @RequestParam String description) {
        if (button.equals("back")) {
            return "Back";
        }
        if (button.equals("open")) {
            Lending l = lendRepo.findById(lendingID);
            l.setConflict(true);
            l.getLendingPerson();
            lendRepo.save(l);
            User owner = l.getLendedArticle().getOwnerUser();
            User admin = userRepo.findById(3);
            send(lendingID, description, (owner.getId()), (l.getLendingPerson().getId()), admin);

        }
        return "conflict-admin-overview";

    }

    @GetMapping("/conflictOverview")
    public String conflictOverview(Model model) {
        List<Lending> lendings = lendRepo.findAllByConflict(true);
        model.addAttribute("lendings", lendings);
        return "conflict-admin-overview";
    }

    @PostMapping("/conflictOverview")
    public String postConflictOverview(Model model,@RequestParam long lendingID, @RequestParam(value = "action") String button){
        if(button.equals("back")){
            return "";
        }
        if(button.equals("show")){
            return "redirect:/showcase/"+lendingID;
        }
        return "";
    }



}
