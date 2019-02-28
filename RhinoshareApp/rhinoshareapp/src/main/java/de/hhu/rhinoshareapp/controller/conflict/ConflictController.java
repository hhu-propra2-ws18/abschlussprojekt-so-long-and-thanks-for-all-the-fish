package de.hhu.rhinoshareapp.controller.conflict;


import de.hhu.rhinoshareapp.Representations.LendingProcessor.APIProcessor;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.ReservationRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class ConflictController {

    @Autowired
    private MailService mailService;

    @Autowired
    LendingRepository lendingRepository;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ReservationRepository reservationRepository;

    APIProcessor apiProcessor = new APIProcessor();


    public void send(long lendId, String conflictMessage, long ownerId, long lenderId, User admin) {
        try {
            mailService.sendConflict(lendId, conflictMessage, ownerId, lenderId);
        } catch (MailException e) {
            //catch error
        }
    }

    @GetMapping("/openConflict/{lendingID}")
    public String openConflict(Model model, Principal p, @PathVariable final long lendingID) {
        model.addAttribute("id", lendingID);
        ActualUserChecker.checkActualUser(model, p, userRepo);
        model.addAttribute("error", " ");
        return "/conflict/conflictUserOpen";
    }

    @PostMapping("/openConflict/{lendingID}")
    public String openConflictpost(Model model, @RequestParam(value = "action") String button, @PathVariable long lendingID,
                                   @RequestParam String description) {
        try {
            if (button.equals("open")) {
                if (!(description.equals(""))) {
                    Optional<Lending> lendList = lendingRepository.findLendingBylendingID(lendingID);
                    Lending l = lendList.get();
                    l.setConflict(true);
                    lendingRepository.save(l);
                    User owner = l.getLendedArticle().getOwner();
                    Optional<User> serviceUser = userRepo.findUserByuserID(3);
                    User admin = serviceUser.get();
                    send(lendingID, description, (owner.getUserID()), (l.getLendingPerson().getUserID()), admin);
                } else {
                    return "redirect:/openConflict";
                }
            }
        } catch (Exception e) {
            model.addAttribute("error", "Something went wrong.");
            return "/conflict/conflictUserOpen";
        }
        return "redirect:/";

    }

    @PostMapping("/admin/{id}")
    public String conflictSolved(@RequestParam(value = "action") String button, @PathVariable long id) {
        Lending lending = lendingRepository.findLendingBylendingID(id).get();
        if (button.equals("winBorrower")) {
            lending.setConflict(false);
            lendingRepository.save(lending);
            //--
            HashMap<String, String> postBodyParas = new HashMap<>();
            postBodyParas.put("lendingID", String.valueOf(lending.getLendingID()));
            postBodyParas.put("decision", "false");
            apiProcessor.punishOrReleaseConflictingLending(postBodyParas, lendingRepository, userRepo, articleRepository, reservationRepository);
            //--
            return "redirect:/admin";
        } else if (button.equals("winOwner")) {
            lending.setConflict(false);
            lendingRepository.save(lending);
            //--
            HashMap<String, String> postBodyParas = new HashMap<>();
            postBodyParas.put("lendingID", String.valueOf(lending.getLendingID()));
            postBodyParas.put("decision", "true");
            apiProcessor.punishOrReleaseConflictingLending(postBodyParas, lendingRepository, userRepo, articleRepository, reservationRepository);
            //--
            return "redirect:/admin";
        }
        return "redirect:/admin";
    }
}
