package de.hhu.rhinoshareapp.web;

import org.springframework.web.bind.annotation.GetMapping;

public class ArtikelController {

    @GetMapping
    public String getArtikelPage() {
        return "artikel";
    }
}
