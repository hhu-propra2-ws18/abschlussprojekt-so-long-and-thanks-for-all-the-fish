package de.hhu.rhinoshareapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginPageController {

    @GetMapping("/login")
    public String loadPage() {
        return "loginpage";
    }

    @RequestMapping("/redirectToSite")
    public String redirect(@RequestParam("redir_url") String redirectUrl) {
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/newaccount")
    public String loadNewAccountPage(Model m) {
        return "createuser";
    }

}
