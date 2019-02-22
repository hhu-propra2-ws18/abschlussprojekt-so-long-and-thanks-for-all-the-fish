package de.hhu.rhinoshareapp.controller.user;

import de.hhu.rhinoshareapp.domain.model.Address;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CreateUserController {

    @Autowired
    public ServiceUserProvider users;

    @PostMapping("/newaccount")
    public String saveNewUser(String name, String surname, String username,
                              String street, String housenumber, String postalcode, String city, String country,
                              String email, String password) {
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
        System.out.println("User: " + username);
        System.out.println("Password: " + password_encrypted);
        return "redirect:/login";
    }

}
