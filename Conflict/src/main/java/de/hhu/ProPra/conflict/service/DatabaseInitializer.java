package de.hhu.ProPra.conflict.service;

import de.hhu.ProPra.conflict.service.Repositorys.ConflictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    ConflictRepository userRepository;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

    }


}
