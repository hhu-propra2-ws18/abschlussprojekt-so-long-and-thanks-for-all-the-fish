package de.hhu.ProPra.conflict;

import de.hhu.ProPra.conflict.controller.MailController;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.ProPra.conflict.model.Article;
import de.hhu.ProPra.conflict.model.Lending;
import de.hhu.ProPra.conflict.model.User;
import de.hhu.ProPra.conflict.service.Repositorys.LendingRepository;
import de.hhu.ProPra.conflict.service.Repositorys.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ConflictApplicationTests {
    @Autowired
    private MailController controller;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    Model m;

    @MockBean
    LendingRepository lendRepo;

    @MockBean
    UserRepository userRepo;

    @Before
    public void setUp(){

        User testUser1 = new User("jeff", "Jeff", "hoffmannfraenz@gmail.com", false, "1, zuhause str, Dueseldorf");
        User testUser2 = new User("george", "George", "hoffmannfraenz@gmail.com", false, "2, lupo str, Duesseldorf");
        Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 2, 500, false, testUser1, testUser2);

        userRepo.save(testUser1);
        userRepo.save(testUser2);
        Mockito.when(userRepo.findByUsername("borri"))
                .thenReturn(testUser1);
        Mockito.when(userRepo.findByUsername("conmi"))
                .thenReturn(testUser2);
        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.set(2019, 1, 12);
        date2.set(2019, 2, 12);
        Lending l = new Lending(date1, date2, testUser1, testArticle1);
        lendRepo.save(l);



        controller.setLendRepo(lendRepo);
        m = Mockito.mock(Model.class);

    }

    @Test
    public void contexLoads() throws Exception {
        assertNotEquals(null, controller);
    }

    @Test
    public void testGetMapping() throws Exception {
        mockMvc.perform(get("/openConflict")).andExpect(status().isOk());
    }



}


