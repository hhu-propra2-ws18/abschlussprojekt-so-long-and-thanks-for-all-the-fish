package com.tests.DatabaseInitializer.dataaccess;

import com.tests.DatabaseInitializer.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    PersonRepository personen;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException { //hier wird die Datenbank gefüllt
        System.out.println("Populating the database");

        Person Daniel = new Person("Daniel");
        Person Jacques = new Person("Jacques");

        personen.saveAll(Arrays.asList(Daniel, Jacques));
    }
}
