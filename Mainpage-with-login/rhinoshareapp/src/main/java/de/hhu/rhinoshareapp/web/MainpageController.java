package de.hhu.rhinoshareapp.web;

import de.hhu.rhinoshareapp.security.database.ServiceUser;
import de.hhu.rhinoshareapp.security.database.ServiceUserProvider;
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
        Optional<ServiceUser> u = users.findByUsername(p.getName());
        ServiceUser user = u.get();
        m.addAttribute("username", user.getSurname());
        return "mainpage";
    }

}
