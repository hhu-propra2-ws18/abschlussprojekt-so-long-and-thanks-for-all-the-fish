package de.hhu.rhinoshareapp.controller;


import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    LendingRepository lendRepo;

    @Autowired
    ServiceUserProvider userRepo;

    public void setUserRepository(ServiceUserProvider userRepository) {
        this.userRepo = userRepository;
    }

    public void setLendingRepository(LendingRepository lendingRepository) {
        this.lendRepo = lendRepo;
    }

    public void setMailService(MailService mailService){
        this.mailService = mailService;
    }

    public void send(long lendId, String conflictMessage, long ownerId, long lenderId, ServiceUser admin) {
        try {
            mailService.sendTest(lendId, conflictMessage, ownerId, lenderId, admin);
        } catch (MailException e) {
            //catch error
        }
    }

    @GetMapping("/openConflict")
    public String openConflict(Model model) {
        return "conflictUserOpen";
    }

    @PostMapping("/openConflict")
    public String openConflictpost(Model model, @RequestParam(value = "action") String button, @RequestParam long lendingID,
                                   @RequestParam String description) {
        if (button.equals("open")) {
            if (!(description.equals(""))) {
                Optional<Lending> lendList = lendRepo.findLendingBylendingID(lendingID);
                Lending l = lendList.get();
                l.setConflict(true);
                l.getLendingPerson();
                lendRepo.save(l);
                ServiceUser owner = l.getLendedArticle().getOwnerUser();
                Optional<ServiceUser> serviceUser = userRepo.findUserByuserID(3);
                ServiceUser admin = serviceUser.get();
                send(lendingID, description, (owner.getUserID()), (l.getLendingPerson().getUserID()), admin);
            } else {
                return "redirect:/openConflict";
            }
        }
        return "redirect:/";

    }


    @GetMapping("/conflictOverview")
    public String conflictOverview(Model model) {
        List<Lending> lendings = lendRepo.findAllByIsConflict(true);
        model.addAttribute("lendings", lendings);
        return "/conflict/conflict-admin-overview";
    }

    @PostMapping("/conflictOverview")
    public String postConflictOverview(Model model, @RequestParam long lendingID, @RequestParam(value = "action") String button) {
        if (button.equals("back")) {
            return "redirect:/";
        }
        if (button.equals("show")) {
            return "redirect:/showcase/" + lendingID;
        }
        return "redirect:/conflictOverview";
    }

    @GetMapping("/showcase/{id}")
    public String getShowCase(Model model, @PathVariable long id) {

        try {
            Optional<Lending> lendlist = lendRepo.findLendingBylendingID(id);
            Lending l = lendlist.get();
            model.addAttribute("owningPerson", l.getLendedArticle().getOwnerUser().getUsername());
            model.addAttribute("borrowwPerson", l.getLendingPerson().getUsername());
            model.addAttribute("lendingID", l.getLendingID());
            model.addAttribute("articleName", l.getLendedArticle().getName());
        } catch (Exception e) {
            return "redirect:/conflictOverview";
        }

        return "/conflict/conflict-admin-case";
    }

    @PostMapping("/showcase/{id}")
    public String conflictSolved(Model model, @RequestParam(value = "action") String button, @PathVariable long id) {
        Optional<Lending> lendlist = lendRepo.findLendingBylendingID(id);
        Lending l = lendlist.get();
        if (button.equals("winBorrower")) {
            l.setConflict(false);
            lendRepo.save(l);
            return "redirect:/borrowerWin";
        } else if (button.equals("winOwner")) {
            l.setConflict(false);
            lendRepo.save(l);
            return "redirect:/ownerWin";
        }
        return "redirect:/conflictOverview";
    }
}
