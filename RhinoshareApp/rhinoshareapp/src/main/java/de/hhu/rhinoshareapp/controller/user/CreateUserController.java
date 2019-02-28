package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class CreateUserController {

    @Autowired
    public UserRepository userRepository;

    @PostMapping("/newaccount")
    public String saveNewUser(String name, String surname, String username,
                              String street, String housenumber, String postcode, String city, String country,
                              String email, String password) {

        validateUsername(username);
        String password_encrypted = encryptPassword(password);
        Address newUserAddress = buildAddress(street, housenumber, postcode, city, country);
        User newUser = new User(name, surname, newUserAddress, username, email, password_encrypted, "ROLE_USER");

        userRepository.save(newUser);
        System.out.println("User    : " + username);
        System.out.println("Password: " + password_encrypted);

        return "redirect:/login";
    }

    @PostMapping("/admin/newaccount")
    public String saveNewUserAsAdmin(String name, String surname, String username,
                              String street, String housenumber, String postcode, String city, String country,
                              String email, String password, String role) {

        validateUsername(username);
        String password_encrypted = encryptPassword(password);
        Address newUserAddress = buildAddress(street, housenumber, postcode, city, country);
        User newUser = new User(name, surname, newUserAddress, username, email, password_encrypted, role);

        userRepository.save(newUser);
        System.out.println("Created user as admin.");
        System.out.println("User    : " + username);
        System.out.println("Password: " + password_encrypted);
        System.out.println("Role    : " + role);

        return "redirect:/admin/usermanagement";
    }

    public String validateUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
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
