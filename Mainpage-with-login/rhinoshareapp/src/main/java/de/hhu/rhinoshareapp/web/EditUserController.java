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
public class EditUserController {

    @Autowired
    ServiceUserProvider users;

    @GetMapping("/edit")
    public String loadEditPage(Model m, Principal p) {
        Optional<ServiceUser> u = users.findByUsername(p.getName());
        ServiceUser user = u.get();
        m.addAttribute("username", user.getSurname());
        m.addAttribute("surname", user.getSurname());
        m.addAttribute("name", user.getName());
        m.addAttribute("email", user.getEmail());
        return "edituser";
    }
}
