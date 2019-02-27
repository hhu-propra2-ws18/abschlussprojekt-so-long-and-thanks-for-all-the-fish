package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminPageController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    LendingRepository lendingRepository;

    //Conflictmanagement
    @GetMapping("/admin")
    public String conflictOverview(Model model, Principal p) {
        List<Lending> lendingList = lendingRepository.findAllByIsConflict(true);
        model.addAttribute("lendings", lendingList);
        model.addAttribute("mainActive","active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "Admin/admin_conflicthandling";
    }

    @PostMapping("/admin")
    public String postConflictOverview(@RequestParam long lendingID, @RequestParam(value = "action") String button) {
        if (button.equals("show")) {
            return "redirect:/showcase/" + lendingID;
        }
        return "redirect:/admin/conflicthandling";
    }

    @PostMapping("/admin/{id}")
    public String conflictSolved(@RequestParam(value = "action") String button, @PathVariable long id) {
        Lending lending = lendingRepository.findLendingBylendingID(id).get();
        if (button.equals("winBorrower")) {
            lending.setConflict(false);
            lendingRepository.save(lending);
            return "redirect:/borrowerWin";
        } else if (button.equals("winOwner")) {
            lending.setConflict(false);
            lendingRepository.save(lending);
            return "redirect:/ownerWin";
        }
        return "redirect:/admin/conflicthandling";
    }

    //Usermanagement
    @GetMapping("/admin/usermanagement")
    public String loadUserManagement(Model model) {
        List<User> userlist = userRepository.findAll();
        model.addAttribute("userlist", userlist);
        model.addAttribute("userActive","active");
        return "Admin/admin_usermanagement";
    }

    @GetMapping("/admin/usermanagement/edit/{id}")
    public String loadEditForm(@PathVariable long id, Model model) {
        User user = userRepository.findUserByuserID(id).get();
        List<Article> allArticles = articleRepository.findAllByOwner(user);
        model.addAttribute("user", user);
        model.addAttribute("articles", allArticles);
        model.addAttribute("userActive","active");
        return "Admin/admin_userEdit";
    }

    //Edit User as Admin
    @PostMapping("/admin/usermanagement/edit/{id}")
    public String profileOverview(@ModelAttribute("user") User user, Model model, Principal p) {
        User oldUser = userRepository.findByUsername(p.getName()).get();
        Address oldUserAddress = oldUser.getAddress();
        Address userAddress = user.getAddress();
        if (!(user.getSurname().equals(""))) {
            oldUser.setSurname(user.getSurname());
        }
        if (!(user.getName().equals(""))) {
            oldUser.setName(user.getName());
        }
        if (!(user.getEmail().equals(""))) {
            oldUser.setEmail(user.getEmail());
        }
        if (userAddress != null) {
            if (!(userAddress.getStreet().equals(""))) {
                oldUserAddress.setStreet(userAddress.getStreet());
            }
            if (!(userAddress.getStreet().equals(""))) {
                oldUserAddress.setCity(userAddress.getCity());
            }
            if (!(userAddress.getCountry().equals(""))) {
                oldUserAddress.setCountry(userAddress.getCountry());
            }
            if (!(userAddress.getHouseNumber().equals(""))) {
                oldUserAddress.setHouseNumber(userAddress.getHouseNumber());
            }
            if (!(userAddress.getPostCode().equals(""))) {
                oldUserAddress.setPostCode(userAddress.getPostCode());
            }
        }
        userRepository.save(oldUser);
        model.addAttribute(oldUser);
        model.addAttribute("userActive","active");
        return "Admin/admin_userEdit";
    }

    //User löschen
    @GetMapping("/admin/usermanagement/delete/{id}")
    public String deleteUser(Model model, Principal p, @PathVariable long id) {
        ActualUserChecker.checkActualUser(model, p, userRepository);
        userRepository.deleteById(id);
        return "redirect:/admin/usermanagement/";
    }

    //User erstellen
    @GetMapping("/admin/usermanagement/add")
    public String addUser(Model model) {
        List<User> userList = userRepository.findAll();
        String idAsString = "" + (userList.size() + 1);
        model.addAttribute("nextID", idAsString);
        model.addAttribute("userActive","active");
        return "Admin/admin_createUser";
    }

    //Artikelmanagement
    @GetMapping("/admin/articlemanagement")
    public String loadArticleManagement(Model model) {
        List<Article> allArticles = articleRepository.findAll();
        model.addAttribute("articles", allArticles);
        model.addAttribute("articleActive","active");
        return "Admin/admin_articlemanagement";
    }

    @GetMapping("/admin/articlemanagement/delete/{id}")
    public String deleteArticle(@PathVariable long id, Model model) {
        articleRepository.delete(articleRepository.findById(id).get());
        model.addAttribute("articleActive","active");
        return "redirect:/admin/articlemanagement/";
    }

    //Ausleihmanagement
    @GetMapping("/admin/lendingmanagement")
    public String loadLendingManagement(Model model) {
        List<Lending> allLendings = lendingRepository.findAll();
        model.addAttribute("lendings", allLendings);
        model.addAttribute("lendingActive","active");
        return "Admin/admin_lendingmanagement";
    }

    @GetMapping("/admin/lendingmanagement/delete/{id}")
    public String deleteLending(@PathVariable long id, Model model) {
        lendingRepository.delete(lendingRepository.findLendingBylendingID(id).get());
        model.addAttribute("lendingActive","active");
        return "redirect:/admin/lendingmanagement/";
    }
}
