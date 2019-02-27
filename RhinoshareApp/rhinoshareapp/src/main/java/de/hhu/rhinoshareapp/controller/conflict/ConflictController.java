package de.hhu.rhinoshareapp.controller.conflict;


import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ConflictController {

    @Autowired
    private MailService mailService;

    @Autowired
    LendingRepository lendRepo;

    @Autowired
    UserRepository userRepo;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    public void setLendingRepository(LendingRepository lendingRepository) {
        this.lendRepo = lendingRepository;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void send(long lendId, String conflictMessage, long ownerId, long lenderId, User admin) {
        try {
            mailService.sendConflict(lendId, conflictMessage, ownerId, lenderId);
        } catch (MailException e) {
            //catch error
        }
    }

    @GetMapping("/openConflict")
    public String openConflict(Model model, Principal p) {
        ActualUserChecker.checkActualUser(model, p, userRepo);
        model.addAttribute("error", " ");
        return "/conflict/conflictUserOpen";
    }

    @PostMapping("/openConflict")
    public String openConflictpost(Model model, @RequestParam(value = "action") String button, @RequestParam long lendingID,
                                   @RequestParam String description) {
        try {
            if (button.equals("open")) {
                if (!(description.equals(""))) {
                    Optional<Lending> lendList = lendRepo.findLendingBylendingID(lendingID);
                    Lending l = lendList.get();
                    l.setConflict(true);
                    lendRepo.save(l);
                    User owner = l.getLendedArticle().getOwner();
                    Optional<User> serviceUser = userRepo.findUserByuserID(3);
                    User admin = serviceUser.get();
                    send(lendingID, description, (owner.getUserID()), (l.getLendingPerson().getUserID()), admin);
                } else {
                    return "redirect:/openConflict";
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong. Probably you entered a wrong lending id.");
            return "/conflict/conflictUserOpen";
        }
        return "redirect:/";

    }


    @GetMapping("/admin/conflicthandling")
    public String conflictOverview(Model model, Principal p) {
        ActualUserChecker.checkActualUser(model, p, userRepo);
        List<Lending> lendings = lendRepo.findAllByIsConflict(true);
        model.addAttribute("lendings", lendings);

        return "Admin/admin_conflicthandling";
    }

    @PostMapping("/admin/conflicthandling")
    public String postConflictOverview(@RequestParam long lendingID, @RequestParam(value = "action") String button) {
        if (button.equals("show")) {
            return "redirect:/showcase/" + lendingID;
        }
        return "redirect:/admin/conflicthandling";
    }

    @GetMapping("admin/conflicthandling/showcase/{id}")
    public String getShowCase(Model model, @PathVariable long id, Principal p) {
        ActualUserChecker.checkActualUser(model, p, userRepo);
        try {
            Optional<Lending> lendlist = lendRepo.findLendingBylendingID(id);
            Lending l = lendlist.get();
            model.addAttribute("owningPerson", l.getLendedArticle().getOwner().getUsername());
            model.addAttribute("borrowwPerson", l.getLendingPerson().getUsername());
            model.addAttribute("lendingID", l.getLendingID());
            model.addAttribute("articleName", l.getLendedArticle().getName());
        } catch (Exception e) {
            return "redirect:/conflictOverview";
        }

        return "/admin/admin_conflicthandlingdetails";
    }

    @PostMapping("admin/conflicthandling/showcase/{id}")
    public String conflictSolved(@RequestParam(value = "action") String button, @PathVariable long id) {
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
        return "redirect:/admin/conflicthandling";
    }
}
