package com.tests.DatabaseInitializer.controller;

import com.tests.DatabaseInitializer.dataaccess.PersonRepository;
import com.tests.DatabaseInitializer.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/")
    public String getMainpage(Model model) {
        List<Person> personen = personRepository.findAll();
        model.addAttribute("personen", personen);
        return "Mainpage";
    }

    @GetMapping("/add")
    public String addPersonWebsiteGetter (){
        return "addNewPerson";
    }

    @PostMapping("/add")
    public String postObjectInDatabase(String name) {
        personRepository.save(new Person(name));
        return "addNewPerson";
    }
}
