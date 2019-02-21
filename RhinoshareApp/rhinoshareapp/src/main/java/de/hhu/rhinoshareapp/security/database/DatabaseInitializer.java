package de.hhu.rhinoshareapp.security.database;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Arrays;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    ServiceUserProvider users;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException { //hier wird die Datenbank gefüllt
        System.out.println("Populating the database");

        ServiceUser root = new ServiceUser("Pasquier", "Jacques", "jacques", "japas102@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");
        ServiceUser joe = new ServiceUser("Test", "Test", "user", "test102@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");
        ServiceUser jacques = new ServiceUser("Test", "Test", "2", "jtest111@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");

        users.saveAll(Arrays.asList(root, joe, jacques));
    }
}
