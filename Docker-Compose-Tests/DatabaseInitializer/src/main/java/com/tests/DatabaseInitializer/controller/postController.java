package com.tests.DatabaseInitializer.controller;

import com.tests.DatabaseInitializer.dataaccess.PersonRepository;
import com.tests.DatabaseInitializer.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class postController {

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/add")
    public String addPersonWebsiteGetter (){
        return "addNewPerson";
    }

    @PostMapping("/add")
    public String postObjectInDatabase(@ModelAttribute("person") Person person) {
        personRepository.save(person);
        return "addNewPerson";
    }
}
