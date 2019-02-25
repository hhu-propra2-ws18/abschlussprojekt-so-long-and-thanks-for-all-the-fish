package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CreateUserController {

    @Autowired
    public UserRepository users;

    @PostMapping("/newaccount")
    public String saveNewUser(String name, String surname, String username,
                              String street, String housenumber, String postalcode, String city, String country,
                              String email, String password) {

        List<User> userList = users.findAll();
        int counter = 0;
        for (User userFromList : userList) {
            if (userFromList.getUsername().equals(username)) {
                counter++;
            }
        }
        if (counter < 1 && username.length() >= 1) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password_encrypted = passwordEncoder.encode(password);
            Address newUserAddress = Address.builder()
                    .street(street)
                    .houseNumber(housenumber)
                    .postCode(postalcode)
                    .city(city)
                    .country(country)
                    .build();
            User newUser = new User(name, surname, newUserAddress, username, email, password_encrypted, "ROLE_USER");

            users.save(newUser);
            System.out.println("User    : " + username);
            System.out.println("Password: " + password_encrypted);
        }
        else System.out.println("Username already exist..");

        return "redirect:/login";
    }

    @PostMapping("/admin/newaccount")
    public String saveNewUserAsAdmin(String name, String surname, String username,
                              String street, String housenumber, String postcode, String city, String country,
                              String email, String password, String role) {

        List<User> userList = users.findAll();
        int counter = 0;
        for (User userFromList : userList) {
            if (userFromList.getUsername().equals(username)) {
                counter++;
            }
        }

        if (counter < 1 && username.length() >= 1) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password_encrypted = passwordEncoder.encode(password);
            Address newUserAddress = Address.builder()
                    .street(street)
                    .houseNumber(housenumber)
                    .postCode(postcode)
                    .city(city)
                    .country(country)
                    .build();
            User newUser = new User(name, surname, newUserAddress, username, email, password_encrypted, role);

            users.save(newUser);
            System.out.println("Created user as admin.");
            System.out.println("User    : " + username);
            System.out.println("Password: " + password_encrypted);
            System.out.println("Role    : " + role);
        }

        else System.out.println("Username already exist..");
        return "redirect:/admin/usermanagement";
    }

}
