package de.hhu.rhinoshareapp.domain.security;

import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

public class ActualUserChecker {

    public static void checkActualUser(Model m, Principal p, UserRepository users) {
        if (p != null) {
            Optional<Person> u = users.findByUsername(p.getName());
            Person person = u.get();
            m.addAttribute("surname", person.getSurname());
            m.addAttribute("role", person.getRole());
            m.addAttribute("loggedIn", "true");
        }
        else {
            m.addAttribute("surname", "Account erstellen");
            m.addAttribute("loggedIn", "false");
        }
    }

}
