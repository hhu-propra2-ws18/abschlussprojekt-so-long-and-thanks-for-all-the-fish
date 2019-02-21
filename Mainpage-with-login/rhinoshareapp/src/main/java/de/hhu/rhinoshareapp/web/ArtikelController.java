package de.hhu.rhinoshareapp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArtikelController {

    @GetMapping("/artikel")
    public String getArtikelPage() {
        return "artikel";
    }
}
