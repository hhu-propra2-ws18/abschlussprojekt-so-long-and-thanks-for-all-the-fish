package de.ProPra.Benutzer.Controller;

import de.ProPra.Benutzer.model.User;
import de.ProPra.Benutzer.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping(path = "/")
    public String showLogin(Model model) {
        return "start";
    }

    @PostMapping(path = "/")
    public String showView(Model model, @RequestParam String username, @RequestParam(value = "action") String user) {
       return "";
    }

    public String signIn(String username){

            return "redirect:/Ausleih";

    }

    public void signUp(String username, String email, String name){
            boolean isAdmin=false;
            User user = new User(username,name,email,isAdmin);
            //insert user to db
        signIn(username);
    }
}
