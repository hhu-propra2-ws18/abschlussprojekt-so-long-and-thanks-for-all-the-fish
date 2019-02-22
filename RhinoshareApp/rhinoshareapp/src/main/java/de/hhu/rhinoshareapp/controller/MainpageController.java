package de.hhu.rhinoshareapp.controller;

import de.hhu.rhinoshareapp.domain.security.ActualUserChecker;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainpageController {

    @Autowired
    ServiceUserProvider users;

    @GetMapping("/")
    public String loadMainPage(Model m, Principal p) {
        ActualUserChecker.checkActualUser(m, p, users);
        return "mainpage";
    }

}
