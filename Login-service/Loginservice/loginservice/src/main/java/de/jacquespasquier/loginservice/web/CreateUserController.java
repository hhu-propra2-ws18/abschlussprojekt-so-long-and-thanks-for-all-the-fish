package de.jacquespasquier.loginservice.web;

import de.jacquespasquier.loginservice.security.UserService;
import de.jacquespasquier.loginservice.security.database.ServiceUser;
import de.jacquespasquier.loginservice.security.database.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CreateUserController {

    @Autowired
    public ServiceUserProvider users;

    @PostMapping("/newaccount")
    public String saveNewUser(String name, String surname, String username, String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password_encrypted = passwordEncoder.encode(password);
        ServiceUser newUser = new ServiceUser(name, surname, username, password_encrypted, "ROLE_ADMIN");
        users.save(newUser);
        System.out.println("User: " + username);
        System.out.println("Password: " + password_encrypted);
        return "redirect: /login";
    }

}
