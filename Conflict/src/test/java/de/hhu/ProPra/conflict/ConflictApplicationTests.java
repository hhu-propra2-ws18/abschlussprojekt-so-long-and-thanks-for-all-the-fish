package de.hhu.ProPra.conflict;

import de.hhu.ProPra.conflict.controller.MailController;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.hhu.ProPra.conflict.model.Article;
import de.hhu.ProPra.conflict.model.Lending;
import de.hhu.ProPra.conflict.model.User;
import de.hhu.ProPra.conflict.model.Article;
import de.hhu.ProPra.conflict.model.Lending;
import de.hhu.ProPra.conflict.model.User;
import de.hhu.ProPra.conflict.service.MailService;
import de.hhu.ProPra.conflict.service.Repositorys.ArticleRepository;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertNotEquals;



@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@WebMvcTest
public class ConflictApplicationTests {
    @Autowired
    MailController controller;

    @Autowired
    MockMvc mvc;

    @MockBean
    Model m;

    @MockBean
    LendingRepository lendingRepo;

    @MockBean
    UserRepository userRepo;

    @MockBean
    ArticleRepository articleRepo;

    @MockBean
    MailService mailService;

    @Before
    public void setUp() {
        User testUser1 = new User("jeff", "Jeff", "hoffmannfraenz@gmail.com", false, "1, zuhause str, Dueseldorf");
        User testUser2 = new User("george", "George", "hoffmannfraenz@gmail.com", false, "2, lupo str, Duesseldorf");
        User testUser3 = new User("franz", "Franz", "hoffmannfraenz@gmail.com", true, "3, heinrich heine allee, Duesseldorf");

        Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 2, 500, false, testUser1, testUser2);
        Article testArticle2 = new Article("Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 10, 250, false, testUser2, testUser3);
        Article testArticle3 = new Article("Grillkohle", "schon verbrannt", 816230, 25230, false, testUser3, testUser1);

        userRepo.save(testUser1);
        userRepo.save(testUser2);
        userRepo.save(testUser3);
        articleRepo.save(testArticle1);
        articleRepo.save(testArticle2);
        articleRepo.save(testArticle3);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();
        date1.set(2019, 1, 12);
        date2.set(2019, 1, 12);
        Calendar date3 = Calendar.getInstance();
        Calendar date4 = Calendar.getInstance();
        date3.set(2019, 4, 12);
        date4.set(2019, 1, 12);
        Lending testLending1 = new Lending(date4, date3, testUser1, testArticle2);
        Lending testLending2 = new Lending(date3, date4, testUser2, testArticle1);
        testLending2.setConflict(true);
        lendingRepo.save(testLending1);
        lendingRepo.save(testLending2);

        Mockito.when(userRepo.findByUsername("jeff"))
                .thenReturn(testUser1);
        Mockito.when(userRepo.findByUsername("george"))
                .thenReturn(testUser2);
        Mockito.when(userRepo.findByUsername("franz"))
                .thenReturn(testUser3);
        Mockito.when(userRepo.findById(1)).thenReturn(testUser1);
        Mockito.when(userRepo.findById(2)).thenReturn(testUser2);
        Mockito.when(userRepo.findById(3)).thenReturn(testUser3);
        Mockito.when(lendingRepo.findById(1)).thenReturn(testLending1);

        controller.setUserRepository(userRepo);
        controller.setLendingRepository(lendingRepo);
        controller.setMailService(mailService);
        m = Mockito.mock(Model.class);

    }

    @Test
    public void contexLoads() throws Exception {
        assertNotEquals(null, controller);
    }

    @Test
    public void testGetMapping() throws Exception {
        mvc.perform(get("/openConflict")).andExpect(status().isOk());
    }

	@Test
	public void openConflictTest(){
		assertEquals("redirect:/openConflict", controller.openConflictpost(m, "open", 7, ""));
		assertEquals("redirect:/", controller.openConflictpost(m, "notOpen", 7, ""));
	}

	@Test
	public void postConflictOverviewTest(){
    	assertEquals("redirect:/", controller.postConflictOverview(m, 1, "back"));
    	assertEquals("redirect:/showcase/1", controller.postConflictOverview(m, 1, "show"));
    	assertEquals("redirect:/conflictOverview", controller.postConflictOverview(m, 1, "no"));
	}

	@Test
	public void getShowcaseTest(){
    	assertEquals("conflict-admin-case", controller.getShowCase(m,1));
    	assertEquals("redirect:/conflictOverview", controller.getShowCase(m,2));
	}


}


