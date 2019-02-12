package de.ProPra.Benutzer.service;

import de.ProPra.Benutzer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    UserRepository userRepository;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("Population the Database");
        User jeff = new User("jeff", "Jeff", "Jeff@email.com", true);
        User george = new User("george", "George", "George@email.com", false);
        User franz = new User("franz", "Franz", "Franz@email.com", true);

        userRepository.save(jeff);
        userRepository.save(george);
        userRepository.save(franz);
    }
}
