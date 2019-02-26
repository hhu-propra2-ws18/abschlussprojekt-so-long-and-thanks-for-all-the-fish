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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/admin")
    public String loadAdminPage() {
        return "Admin/admin_overview";
    }

    //Usermanagement
    @GetMapping("/admin/usermanagement")
    public String loadUserManagement(Model model) {
        List<User> userlist = userRepository.findAll();
        model.addAttribute("userlist", userlist);
        return "Admin/admin_usermanagement";
    }

    @GetMapping("/admin/usermanagement/edit/{id}")
    public String loadEditForm(@PathVariable long id, Model model) {
        User user = userRepository.findUserByuserID(id).get();
        List<Article> allArticles = articleRepository.findAllByOwner(user);
        model.addAttribute("user", user);
        model.addAttribute("articles", allArticles);
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
        return "Admin/admin_userEdit";
    }

    //Eintrag löschen
    @GetMapping("/admin/usermanagement/delete/{id}")
    public String deleteUser(Model model, Principal p, @PathVariable long id) {
        ActualUserChecker.checkActualUser(model, p, userRepository);
        userRepository.deleteById(id);
        return "redirect:/admin/usermanagement/";
    }

    @GetMapping("/admin/usermanagement/add")
    public String addUser(Model model) {
        List<User> userList = userRepository.findAll();
        String idAsString = "" + (userList.size() + 1);
        model.addAttribute("nextID", idAsString);
        return "Admin/admin_createUser";
    }

    //Artikelmanagement
    @GetMapping("/admin/articlemanagement")
    public String loadArticleManagement(Model model) {
        List<Article> allArticles = articleRepository.findAll();
        model.addAttribute("articles", allArticles);
        return "Admin/admin_articlemanagement";
    }

    @GetMapping("/admin/articlemanagement/delete/{id}")
    public String deleteArticle(@PathVariable long id) {
        articleRepository.delete(articleRepository.findById(id).get());
        return "redirect:/admin/articlemanagement/";
    }

    //Ausleihmanagement
    @GetMapping("/admin/lendingmanagement")
    public String loadLendingManagement(Model m) {
        List<Lending> allLendings = lendingRepository.findAll();
        m.addAttribute("lendings", allLendings);
        return "Admin/admin_lendingmanagement";
    }

    @GetMapping("/admin/lendingmanagement/delete/{id}")
    public String deleteLending(@PathVariable long id) {
        lendingRepository.delete(lendingRepository.findLendingBylendingID(id).get());
        return "redirect:/admin/lendingmanagement/";
    }

}
