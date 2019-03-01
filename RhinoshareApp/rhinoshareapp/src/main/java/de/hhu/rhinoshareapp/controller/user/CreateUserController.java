package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.Representations.LendingProcessor.APIProcessor;
import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class CreateUserController {

    @Autowired
    public UserRepository userRepository;

    @PostMapping("/newaccount")
    public String saveNewUser(Model model, Principal p, String name, String surname, String username,
                              String street, String housenumber, String postcode, String city, String country,
                              String email, String password) {

        validateUsername(username);
        String password_encrypted = encryptPassword(password);
        Address newUserAddress = buildAddress(street, housenumber, postcode, city, country);
        Person newPerson = new Person(name, surname, newUserAddress, username, email, password_encrypted, "ROLE_USER");
        userRepository.save(newPerson);
        APIProcessor apiProcessor = new APIProcessor();
        apiProcessor.getAccountInformationWithId(newPerson.getUserID(), userRepository);
        System.out.println("Person    : " + username);
        System.out.println("Password: " + password_encrypted);
        if (apiProcessor.isErrorOccurred()) {
            model.addAttribute("error", apiProcessor.getErrorMessage().get("reason"));
            apiProcessor.setErrorOccurred(false);
            ActualUserChecker.checkActualUser(model, p, userRepository);
            return "Lending/errorPage";
        }
        return "redirect:/login";
    }

    @PostMapping("/admin/newaccount")
    public String saveNewUserAsAdmin(String name, String surname, String username,
                              String street, String housenumber, String postcode, String city, String country,
                              String email, String password, String role) {

        validateUsername(username);
        String password_encrypted = encryptPassword(password);
        Address newUserAddress = buildAddress(street, housenumber, postcode, city, country);
        Person newPerson = new Person(name, surname, newUserAddress, username, email, password_encrypted, role);

        userRepository.save(newPerson);
        System.out.println("Created user as admin.");
        System.out.println("Person    : " + username);
        System.out.println("Password: " + password_encrypted);
        System.out.println("Role    : " + role);

        return "redirect:/admin/usermanagement";
    }

    public String validateUsername(String username) {
        Optional<Person> user = userRepository.findByUsername(username);
        if (user.isPresent() || username.length() >= 1)
            return "error/usernameExists";
        return "";
    }

    public String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    public Address buildAddress(String street, String housenumber, String postcode, String city, String country) {
        Address address = Address.builder()
                .street(street)
                .houseNumber(housenumber)
                .postCode(postcode)
                .city(city)
                .country(country)
                .build();
        return address;
    }

}
