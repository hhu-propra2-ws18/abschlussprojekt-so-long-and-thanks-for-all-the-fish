package de.hhu.rhinoshareapp.controller;

import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MainpageController {

    @Autowired
    ServiceUserProvider users;

    @GetMapping("/")
    public String loadMainPage(Model m, Principal p) {
        if (p != null) {
            Optional<ServiceUser> u = users.findByUsername(p.getName());
            ServiceUser user = u.get();
            m.addAttribute("surname", user.getSurname());
            m.addAttribute("loggedIn", "true");
        }
        else {
            m.addAttribute("surname", "Account erstellen");
            m.addAttribute("loggedIn", "false");
        }
        return "mainpage";
    }

}
