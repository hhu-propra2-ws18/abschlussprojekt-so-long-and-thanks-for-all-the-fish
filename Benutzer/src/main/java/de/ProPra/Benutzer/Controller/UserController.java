package de.ProPra.Benutzer.Controller;

import de.ProPra.Benutzer.model.User;
import de.ProPra.Benutzer.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/")
    public String showLogin(Model model) {
        return "start";
    }

    @PostMapping(path = "/")
    public String showView(Model model, @RequestParam String username, @RequestParam(value = "action") String button) {
        if (button.equals("Signup")) {
            return "redirect:/signup";
        } else if (button.equals("Login")) {
            return signIn(model, username);
        }
        return "";
    }

    @GetMapping(path = "/user/{id}")
    public String loggedIn(Model model, @PathVariable long id) {
        return "dummyhome";
    }

    @PostMapping(path = "/user/{id}")
    public String home(Model model, @RequestParam(value = "action") String button, @PathVariable(value = "id") long id) {

        if (button.equals("Profile overview")) {
            return "redirect:/profileOverview/" + id;
        }
        return "/user/" + id;
    }

    @GetMapping(path = "/profileOverview/{id}")
    public String profileOverview(Model model, @PathVariable(value = "id") long id) {
        User user = userRepository.findById(id);
        model.addAttribute("error", " ");
        model.addAttribute("user", user);
        return "profileoverview";
    }

    @PostMapping(path = "/profileOverview/{id}")
    public String profileOverview(Model model, @RequestParam(value = "action") String button, @PathVariable(value = "id") long id
            , @RequestParam String username, @RequestParam String name, @RequestParam String email, @RequestParam String address) {
        User user = userRepository.findById(id);
        if (button.equals("back to Dummyhome")) {
            return "redirect:/user/" + id;
        } else if (button.equals("Apply changes")) {
            if (username.equals("") == false) {
                if (isValidUsername(username)) {
                    if (!(isUsernameExistend(username))) {
                        user.setUsername(username);
                    } else {
                        model.addAttribute("error", "Username already taken ");
                    }
                } else {
                    model.addAttribute("error", "Do not use spaces in your username please");
                    return "profileoverview";
                }
            }
            if (name.equals("") == false) {
                user.setName(name);
            }

            if (email.equals("") == false) {
                if (isValidEmailAddress(email)) {
                    user.setEmail(email);
                } else {
                    model.addAttribute("error", "Email is not valid!");
                    return "profileoverview";
                }
            }
            if (address.equals("") == false) {
                if (true) {
                    user.setAddress(address);
                } else {
                    model.addAttribute("error", "Address is not valid!");
                    return "profileoverview";
                }
            }
            userRepository.save(user);
        }
        model.addAttribute("error", " ");
        model.addAttribute("user", user);
        return "profileoverview";
    }


    @GetMapping(path = "/signup")
    public String showSignUp(Model model) {
        model.addAttribute("error", "");
        return "signUp";
    }

    @PostMapping(path = "/signup")
    public String signUp(Model model, @RequestParam String username, @RequestParam String
            name, @RequestParam String email, @RequestParam String address, @RequestParam(value = "action") String button) {
        if (button.equals("backToLogin")) {
            return "redirect:/";
        }
        boolean isAdmin = false;
        if (username.equals("") || name.equals("") || email.equals("") || address.equals("")) {
            model.addAttribute("error", "All fields must be filled ! ");
            return "signUp";
        } else if (!(isValidUsername(username))) {
            model.addAttribute("error", "Do not use spaces in your username please");
            return "signUp";
        } else if (!(isUsernameExistend(username))) {
            User user = new User(username, name, email, isAdmin, address);
            if (isValidEmailAddress(user.getEmail())) {
                user = userRepository.save(user);
                return "redirect:/user/" + userRepository.findByUsername(username).getId();
            } else {
                model.addAttribute("error", "Email is not valid!");
                return "signUp";
            }
        } else {
            model.addAttribute("error", "Username already taken ");
            return "signUp";
        }


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

    public boolean isUsernameExistend(String username) {
        if (userRepository.findByUsername(username) != null) {
            return true;
        }
        return false;
    }

    public String signIn(Model model, String username) {
        if (isUsernameExistend(username)) {
            model.addAttribute("username", username);
            return "redirect:/user/" + userRepository.findByUsername(username).getId();
        } else {
            return "start";
        }
    }

}
