package de.hhu.rhinoshareapp.domain.database;


import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
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

        ServiceUser root = new ServiceUser("Pasquier", "Jacques", "Musterstr. 8, 41749 Viersen", "jacques", "japas102@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");
        ServiceUser joe = new ServiceUser("Test", "Test", "Musterstr. 8, 41749 Viersen", "user", "test102@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_USER");
        ServiceUser jacques = new ServiceUser("Test", "Test", "Musterstr. 8, 41749 Viersen", "2", "jtest111@hhu.de", "$2a$08$MbCSKfkg1C9A6mx82wwVneBpUkyW1ZwhsEjorhqkMYrhRxLJDZ9yO", "ROLE_ADMIN");

        users.saveAll(Arrays.asList(root, joe, jacques));
    }
}
