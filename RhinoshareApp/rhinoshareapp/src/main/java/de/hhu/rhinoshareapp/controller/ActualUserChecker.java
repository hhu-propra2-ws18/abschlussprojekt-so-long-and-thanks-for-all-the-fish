package de.hhu.rhinoshareapp.controller;

import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

public class ActualUserChecker {

    static void checkActualUser(Model m, Principal p, ServiceUserProvider users) {
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
    }

}
