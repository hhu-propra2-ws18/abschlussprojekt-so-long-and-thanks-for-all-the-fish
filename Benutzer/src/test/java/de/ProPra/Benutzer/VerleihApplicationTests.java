package de.ProPra.Benutzer;

import de.ProPra.Benutzer.Controller.UserController;
import de.ProPra.Benutzer.model.User;

import de.ProPra.Benutzer.service.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@WebMvcTest
public class VerleihApplicationTests {

    @Autowired
    MockMvc mvc;


    @MockBean
    UserRepository userRepo;

    @Before
    public void setUp() {


        User user1 = new User("borri", "Borri", "heinrich@gmail.com", true, "12 , unistrasse , Mülheim");
        User user2 = new User("conmi", "Mike C", "klettermaus@gmail.com", false, "11 , unistrasse, wuppertal");

        userRepo.save(user1);
        userRepo.save(user2);
        Mockito.when(userRepo.findByUsername("borri"))
                .thenReturn(user1);
        Mockito.when(userRepo.findByUsername("conmi"))
                .thenReturn(user2);
        Mockito.when(userRepo.findById(0)).thenReturn(user1);


    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void userConstructorTest() {

        assertEquals("Borri", userRepo.findByUsername("borri").getName());
        assertEquals("heinrich@gmail.com", userRepo.findByUsername("borri").getEmail());
        assertEquals("12 , unistrasse , Mülheim", userRepo.findByUsername("borri").getAddress());
        assertEquals(0, userRepo.findByUsername("borri").getScore());

    }

    @Test
    public void userSetterTest() {
        User user = new User();
        user.setUsername("username");
        user.setName("Name");
        user.setAddress("Zuhause");
        user.setEmail("test@mail.com");
        user.setAdmin(false);
        user.setId(1);
        user.setBankBalance(10);
        user.setScore(0);
        assertEquals("username",user.getUsername());
        assertEquals("Name",user.getName());
        assertEquals("Zuhause",user.getAddress());
        assertEquals("test@mail.com",user.getEmail());
        assertEquals(false,user.isAdmin());
        assertEquals(1,user.getId());
        //assertEquals(10,user.getBankBalance());
        assertEquals(0,user.getScore());
    }

    @Test
    public void retrieve() throws Exception {

        mvc.perform(get("/")).andExpect(status().isOk());
       // id Tests missing
        mvc.perform(get("/signup")).andExpect(status().isOk());

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

