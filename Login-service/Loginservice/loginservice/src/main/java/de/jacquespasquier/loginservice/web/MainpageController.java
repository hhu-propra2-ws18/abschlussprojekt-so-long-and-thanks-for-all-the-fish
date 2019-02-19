package de.jacquespasquier.loginservice.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainpageController {

    @GetMapping("/")
    public String loadtestPage(Model m, Principal p) {

        return "mainpage";
    }

}
