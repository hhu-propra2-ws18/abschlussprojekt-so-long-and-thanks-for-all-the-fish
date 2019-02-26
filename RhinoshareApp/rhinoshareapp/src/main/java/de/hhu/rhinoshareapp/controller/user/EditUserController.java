package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class EditUserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/edit")
    public String loadEditPage(Model model, Principal p) {
        User user = userRepository.findByUsername(p.getName()).get();
        model.addAttribute("user", user);
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "/EditUser/profileOverview";
    }

    @PostMapping("/edit")
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
        return "/EditUser/profileOverview";
    }
}
