package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
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
    UserRepository users;

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
        return "/EditUser/profileOverview";
    }

    @PostMapping(path = "/edit")
    public String profileOverview(Model model, Principal p, @RequestParam(value = "action") String button,@RequestParam String surname,
                                  @RequestParam String username, @RequestParam String name, @RequestParam String email,
                                  @RequestParam String street, @RequestParam String city, @RequestParam String country,
                                  @RequestParam String houseNumber, @RequestParam String postCode) {
        Optional<User> u = users.findByUsername(p.getName());
        User user = u.get();
        Address a = user.getAddress();
        if (button.equals("Back")) {
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
                    return "/EditUser/profileOverview";
                }
            }
            if (surname.equals("") == false) {
                user.setSurname(surname);
            }

            if (name.equals("") == false) {
                user.setName(name);
            }

            if (email.equals("") == false) {
                    user.setEmail(email);
            }

            if (!(street.equals(""))){
                a.setStreet(street);
            }
            if (!(city.equals(""))){
                a.setCity(city);
            }
            if (!(country.equals(""))){
                a.setCountry(country);
            }
            if (!(houseNumber.equals(""))){
                a.setHouseNumber(houseNumber);
            }
            if (!(postCode.equals(""))){
                a.setPostCode(postCode);
            }
            model.addAttribute("user",user);
            users.save(user);
        }
        model.addAttribute("error", " ");
        model.addAttribute("user", user);
        return "/EditUser/profileOverview";
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
