package de.hhu.rhinoshareapp.domain.security;

import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

public class ActualUserChecker {

    public static void checkActualUser(Model m, Principal p, ServiceUserProvider users) {
        if (p != null) {
            Optional<User> u = users.findByUsername(p.getName());
            User user = u.get();
            m.addAttribute("surname", user.getSurname());
            m.addAttribute("loggedIn", "true");
        }
        else {
            m.addAttribute("surname", "Account erstellen");
            m.addAttribute("loggedIn", "false");
        }
    }

}
