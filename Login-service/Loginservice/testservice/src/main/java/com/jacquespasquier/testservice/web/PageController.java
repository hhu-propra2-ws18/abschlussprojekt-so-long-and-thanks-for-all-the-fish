package com.jacquespasquier.testservice.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class PageController {

    @GetMapping("/")
    public String loadPage() {
        return "testpage";
    }
}
