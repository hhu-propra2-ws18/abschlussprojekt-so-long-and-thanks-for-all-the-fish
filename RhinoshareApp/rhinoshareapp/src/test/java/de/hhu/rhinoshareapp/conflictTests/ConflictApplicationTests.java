package de.hhu.rhinoshareapp.conflictTests;

import de.hhu.rhinoshareapp.controller.conflict.ConflictController;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.ArticleRepository;
import de.hhu.rhinoshareapp.domain.service.LendingRepository;
import de.hhu.rhinoshareapp.domain.service.ServiceUserProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@WebMvcTest
public class ConflictApplicationTests {
    @Autowired
    ConflictController controller;

    @Autowired
    MockMvc mvc;

    @MockBean
    Model m;

    @MockBean
    LendingRepository lendingRepo;

    @MockBean
    ServiceUserProvider userRepo;

    @MockBean
    ArticleRepository articleRepo;

    @MockBean
    MailService mailService;

    @Before
    public void setUp() {
        User testUser1 = new User("Jeff", "Nosbusch", "strasse", "jeff", "jeff@mail.com", "1234", "user");
        User testUser2 = new User("George", "Pi", "Blumenstrasse", "george", "george@mail.com", "1234", "user");
        User testUser3 = new User("Franz", "Hoff", "Palmenstrasse", "franz", "franz@mail.com", "1234", "user");

        long id1 = testUser1.getUserID();
        long id2 = testUser2.getUserID();

        Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", id1, 500, 25, true, null);
        Article testArticle2 = new Article("Geschirr", "nur ein bisschen zerbrochen, für 20 mann", id2, 250, 25, true, null);
        Article testArticle3 = new Article("Grillkohle", "schon verbrannt", id2, 25230, 88, false, null);

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

        Lending testLending1 = new Lending(date1,date2,testUser1,testArticle2);
        Lending testLending2 = new Lending(date3, date4, testUser2, testArticle1);
        testLending2.setConflict(true);
        lendingRepo.save(testLending1);
        lendingRepo.save(testLending2);

       /*Optional<ServiceUser> sUser1= (Optional<ServiceUser>) testUser1;

        Mockito.when(userRepo.findByUsername("jeff"))
                .thenReturn(testUser1);
        Mockito.when(userRepo.findByUsername("george"))
                .thenReturn(testUser2);
        Mockito.when(userRepo.findByUsername("franz"))
                .thenReturn(testUser3);
        Mockito.when(userRepo.findById(1)).thenReturn(testUser1);
        Mockito.when(userRepo.findById(2)).thenReturn(testUser2);
        Mockito.when(userRepo.findById(3)).thenReturn(testUser3);
        Mockito.when(lendingRepo.findAllByIsConflict(true)).thenReturn(null);
        Mockito.when(lendingRepo.findLendingBylendedArticle(7)).thenReturn(testLending1);
        Mockito.when(lendingRepo.save(testLending1)).thenReturn(testLending1);
        Mockito.when(lendingRepo.findById(1)).thenReturn(testLending1);


        controller.setUserRepository(userRepo);
        controller.setLendingRepository(lendingRepo);
        controller.setMailService(mailService);
        m = Mockito.mock(Model.class);
*/
    }

    @Test
    public void contexLoads() throws Exception {
        assertNotEquals(null, controller);
    }

    @Test
    public void testGetMapping() throws Exception {
        mvc.perform(get("/openConflict")).andExpect(status().isOk());
        mvc.perform(get("/conflictOverview")).andExpect(status().isOk());

    }

	@Test
	public void openConflictTest(){
		assertEquals("redirect:/openConflict", controller.openConflictpost(m, "open", 7, ""));
		assertEquals("redirect:/", controller.openConflictpost(m, "notOpen", 7, ""));
		assertEquals("redirect:/", controller.openConflictpost(m, "open", 7, "testkgzgjkg"));
	}

	@Test
    public void testPostMappingConflictSolved(){

        assertEquals("redirect:/borrowerWin",controller.conflictSolved(m,"winBorrower",7));
        assertEquals("redirect:/ownerWin",controller.conflictSolved(m,"winOwner",7));
        assertEquals("redirect:/conflictOverview",controller.conflictSolved(m,"",7));
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


