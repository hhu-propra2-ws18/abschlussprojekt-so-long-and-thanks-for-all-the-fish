package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class EditUserController {

    @Autowired
    ServiceUserProvider users;

    @GetMapping("/edit")
    public String loadEditPage(Model m, Principal p) {
        Optional<User> u = users.findByUsername(p.getName());
        User user = u.get();
        /*m.addAttribute("username", user.getUsername());
        m.addAttribute("surname", user.getSurname());
        m.addAttribute("name", user.getName());
        m.addAttribute("email", user.getEmail());
        m.addAttribute("loggedIn", "true");
        return "edituser";*/
        m.addAttribute("error", " ");
        m.addAttribute("user", user);
        return "/EditUser/profileoverview";
    }

    @PostMapping(path = "/edit")
    public String profileOverview(Model model, Principal p, @RequestParam(value = "action") String button,@RequestParam String surname, @RequestParam String username, @RequestParam String name, @RequestParam String email, @RequestParam String address) {
        Optional<User> u = users.findByUsername(p.getName());
        User user = u.get();
        if (button.equals("back to Dummyhome")) {
            return "redirect:/";
        } else if (button.equals("Apply changes")) {
            if (username.equals("") == false) {
                if (isValidUsername(username)) {
                    if (users.findByUsername(username)==null) {
                        user.setUsername(username);
                    } else {
                        model.addAttribute("user",user);
                        model.addAttribute("error", "Username already taken ");
                    }
                } else {
                    model.addAttribute("error", "Do not use spaces in your username please");
                    model.addAttribute("user",user);
                    return "/EditUser/profileoverview";
                }
            }
            if (surname.equals("") == false) {
                user.setSurname(surname);
            }

            if (name.equals("") == false) {
                user.setName(name);
            }

            if (email.equals("") == false) {
                if (isValidEmailAddress(email)) {
                    user.setEmail(email);
                } else {
                    model.addAttribute("error", "Email is not valid!");
                    model.addAttribute("user",user);
                    return "/EditUser/profileoverview";
                }
            }
            if (address.equals("") == false) {
                if (true) {
                    user.setAddress(address);
                } else {
                    model.addAttribute("user",user);
                    model.addAttribute("error", "Address is not valid!");
                    return "/EditUser/profileoverview";
                }
            }
            users.save(user);
        }
        model.addAttribute("error", " ");
        model.addAttribute("user", user);
        return "/EditUser/profileoverview";
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public boolean isValidUsername(String username) {
        if (username.equals("") == false) {
            if (username.contains(" ")) {
                return false;
            }
            return true;
        }
        return false;
    }
}
