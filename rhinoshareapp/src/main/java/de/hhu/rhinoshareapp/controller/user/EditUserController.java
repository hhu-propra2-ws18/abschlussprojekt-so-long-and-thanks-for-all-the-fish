package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class EditUserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/edit")
    public String loadEditPage(Model model, Principal p) {
        Person person = userRepository.findByUsername(p.getName()).get();
        model.addAttribute("user", person);
        model.addAttribute("userActive","active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "EditUser/profileOverview";
    }

    @PostMapping("/edit")
    public String profileOverview(@ModelAttribute("user") Person person, Model model, Principal p) {
        Person oldPerson = userRepository.findByUsername(p.getName()).get();
        Address oldUserAddress = oldPerson.getAddress();
        Address userAddress = person.getAddress();
        if(!(person.getUsername()).equals(""))
            oldPerson.setUsername(person.getUsername());
        oldPerson = setEditedAttributesInUser(person, oldPerson, oldUserAddress, userAddress);
        userRepository.save(oldPerson);
        model.addAttribute(oldPerson);
        model.addAttribute("userActive","active");
        ActualUserChecker.checkActualUser(model, p, userRepository);
        return "EditUser/profileOverview";
    }

    static Person setEditedAttributesInUser(Person person, Person oldPerson, Address oldUserAddress, Address userAddress) {
        if (!(person.getSurname().equals(""))) {
            oldPerson.setSurname(person.getSurname());
        }
        if (!(person.getName().equals(""))) {
            oldPerson.setName(person.getName());
        }
        if (!(person.getEmail().equals(""))) {
            oldPerson.setEmail(person.getEmail());
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
        return oldPerson;
    }
}
