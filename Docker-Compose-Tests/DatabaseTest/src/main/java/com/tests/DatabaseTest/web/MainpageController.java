package com.tests.DatabaseTest.web;

import com.tests.DatabaseTest.DataService;
import com.tests.DatabaseTest.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainpageController {

    @Autowired
    public DataService service;

    @GetMapping(path = "/")
    public String load(Model model) {
        List<Person> personen = service.getNames();
        model.addAttribute("personen", personen);
        return "Mainpage";
    }

}
