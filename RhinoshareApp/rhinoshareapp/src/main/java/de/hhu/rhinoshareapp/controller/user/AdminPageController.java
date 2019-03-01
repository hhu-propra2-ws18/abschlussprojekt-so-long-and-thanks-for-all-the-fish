package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    public String conflictOverview(Model model) {
        List<Lending> lendingList = lendingRepository.findAllByIsConflict(true);
        model.addAttribute("lendings", lendingList);
        model.addAttribute("mainActive","active");
        return "Admin/admin_conflicthandling";
    }

    @PostMapping("/admin")
    public String postConflictOverview(@RequestParam long lendingID, @RequestParam(value = "action") String button) {
        if (button.equals("show")) {
            return "redirect:/showcase/" + lendingID;
        }
        return "redirect:/admin/conflicthandling";
    }

    //Usermanagement
    @GetMapping("/admin/usermanagement")
    public String loadUserManagement(Model model) {
        List<Person> userlist = userRepository.findAll();
        model.addAttribute("userlist", userlist);
        model.addAttribute("userActive","active");
        return "Admin/admin_usermanagement";
    }

    @GetMapping("/admin/usermanagement/edit/{id}")
    public String loadEditForm(@PathVariable long id, Model model) {
        Person person = userRepository.findUserByuserID(id).get();
        List<Article> allArticles = articleRepository.findAllByOwner(person);
        model.addAttribute("user", person);
        model.addAttribute("articles", allArticles);
        model.addAttribute("userActive","active");
        return "Admin/admin_userEdit";
    }

    //Edit Person as Admin
    @PostMapping("/admin/usermanagement/edit/{id}")
    public String profileOverview(@ModelAttribute("user") Person person, Model model, Principal p) {
        Person oldPerson = userRepository.findByUsername(p.getName()).get();
        Address oldUserAddress = oldPerson.getAddress();
        Address userAddress = person.getAddress();
        oldPerson = EditUserController.setEditedAttributesInUser(person, oldPerson, oldUserAddress, userAddress);
        userRepository.save(oldPerson);
        model.addAttribute(oldPerson);
        model.addAttribute("userActive","active");
        return "Admin/admin_userEdit";
    }

    //Person löschen
    @GetMapping("/admin/usermanagement/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/usermanagement/";
    }

    //Person erstellen
    @GetMapping("/admin/usermanagement/add")
    public String addUser(Model model) {
        List<Person> personList = userRepository.findAll();
        String idAsString = "" + (personList.size() + 1);
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
