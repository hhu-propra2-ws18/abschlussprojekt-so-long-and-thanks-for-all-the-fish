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

    public String signIn(Model model, String username) {
        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("username", username);
            return "redirect:/user/" + userRepository.findByUsername(username).getId();
        } else {
            return "start";
        }
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
    public String profileOverview(Model model, @PathVariable(value="id") long id) {
        User user = userRepository.findById(id);
        model.addAttribute("username",user.getUsername());
        model.addAttribute("email",user.getEmail());
        model.addAttribute("name",user.getName());
        return "profileoverview";
    }

    @PostMapping(path = "/profileOverview/{id}")
    public String profileOverview (Model model, @RequestParam(value = "action") String button, @PathVariable(value = "id") long id
                , @RequestParam String username, @RequestParam String name, @RequestParam String email ){
            if (button.equals("back to Dummyhome")) {
                return "redirect:/user/" + id;
            } else if (button.equals("Apply changes")) {
                User user = userRepository.findById(id);
                boolean isAdmin = false;
                if (username.equals("")==false){
                    if(username.contains(" ")){
                        model.addAttribute("error", "Do not use spaces in your username please");
                        return"";
                    }
                    else{
                        if(userRepository.findByUsername(username)==null){
                            user.setUsername(username);
                        }
                        else{
                            model.addAttribute("error", "Username already taken ");
                        }
                    }
                }
                if(name.equals("")==false){
                    user.setName(name);
                }

                if(email.equals("")==false){
                    if(isValidEmailAddress(email)) {
                        user.setEmail(email);
                    }
                    else{
                        model.addAttribute("error", "Email is not valid!");
                        return "profileoverview";
                    }
                }
                userRepository.save(user);
                System.out.println(user.toString());
            }
            return "profileoverview";
        }


        @GetMapping(path = "/signup")
        public String showSignUp (Model model){
            model.addAttribute("error", "");
            return "signUp";
        }

        @PostMapping(path = "/signup")
        public String signUp (Model model, @RequestParam String username, @RequestParam String
        name, @RequestParam String email, @RequestParam(value = "action") String button){
            if (button.equals("backToLogin")) {
                return "redirect:/";
            }
            boolean isAdmin = false;
            if (username.equals("") || name.equals("") || email.equals("")) {
                model.addAttribute("error", "All fields must be filled ! ");
                return "signUp";
            } else if (username.contains(" ")) {
                model.addAttribute("error", "Do not use spaces in your username please");
                return "signUp";
            } else if (userRepository.findByUsername(username) == null) {
                User user = new User(username, name, email, isAdmin);
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

        public boolean isValidEmailAddress (String email){
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email);
            return m.matches();
        }
    }
