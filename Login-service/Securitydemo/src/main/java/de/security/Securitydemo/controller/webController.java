package de.security.Securitydemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class webController {

    @GetMapping("/")
    public String index() {
        return "Du kannst dies nur sehen wenn du eingelogt bist!";
    }
}
