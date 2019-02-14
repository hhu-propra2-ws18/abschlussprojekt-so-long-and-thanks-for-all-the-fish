package de.ProPra.Benutzer;

import de.ProPra.Benutzer.Controller.UserController;
import de.ProPra.Benutzer.model.User;
import de.ProPra.Benutzer.service.DatabaseInitializer;
import de.ProPra.Benutzer.service.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerleihApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void testIsValidEmail() {
        UserController uc = new UserController();
        assertEquals(false, uc.isValidEmailAddress("testil.com"));
        assertEquals(true, uc.isValidEmailAddress("jeff@hhu.de"));
    }

    @Test
    public void testIsValidUsername() {
        UserController uc = new UserController();
        assertEquals(false, uc.isValidUsername("franz hoffmann"));
        assertEquals(true, uc.isValidUsername("franz_hoffmann"));
        assertEquals(false, uc.isValidUsername(""));
    }


}

