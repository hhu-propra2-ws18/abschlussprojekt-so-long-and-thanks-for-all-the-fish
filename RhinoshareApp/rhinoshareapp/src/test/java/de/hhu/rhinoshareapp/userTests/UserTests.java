package de.hhu.rhinoshareapp.userTests;


import de.hhu.rhinoshareapp.Representations.LendingProcessor.APIProcessor;
import de.hhu.rhinoshareapp.controller.MainpageController;
import de.hhu.rhinoshareapp.controller.conflict.ConflictController;
import de.hhu.rhinoshareapp.controller.user.AdminPageController;
import de.hhu.rhinoshareapp.controller.user.CreateUserController;
import de.hhu.rhinoshareapp.controller.user.EditUserController;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.User;
import de.hhu.rhinoshareapp.domain.service.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@WebMvcTest
public class UserTests {
    @Autowired
    AdminPageController controller;

    @Autowired
    CreateUserController cController;

    @Autowired
    EditUserController editUsercontroller;

    @Autowired
    MainpageController cm;

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
    ImageRepository imageRepo;

    @MockBean
    MailService mailService;

    @MockBean
    ReservationRepository reserveRepo;

    @MockBean
    TransactionRepository transRepo;

    @Mock
    Principal p;

    @MockBean
    APIProcessor api;


    @Before
    public void setUp() {

        User testUser1 = new User("Jeff", "Nosbusch", null, "jeff", "jeff@mail.com", "1234", "user");
        User testUser2 = new User("George", "Pi", null, "george", "george@mail.com", "1234", "user");
        User testUser3 = new User("Franz", "Hoff", null, "franz", "franz@mail.com", "1234", "user");

        long id1 = testUser1.getUserID();
        long id2 = testUser2.getUserID();

        Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 500, 25, true, null);
        Article testArticle2 = new Article("Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 250, 25, true, null);
        Article testArticle3 = new Article("Grillkohle", "schon verbrannt", 25230, 88, false, null);

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

        Lending testLending1 = new Lending(date1, date2, testUser1, testArticle2);
        Lending testLending2 = new Lending(date3, date4, testUser2, testArticle1);
        testLending2.setConflict(true);
        testLending1.getLendedArticle().setOwner(testUser3);
        lendingRepo.save(testLending1);
        lendingRepo.save(testLending2);

        Optional<User> oUser1 = Optional.of(testUser1);
        Optional<User> oUser2 = Optional.of(testUser2);
        Optional<User> oUser3 = Optional.of(testUser3);
        Optional<Lending> oLending1 = Optional.of(testLending1);
        Optional<Lending> oLending2 = Optional.of(testLending2);
        Optional<Article> oArticle = Optional.of(testArticle1);

        List<Optional<Lending>> alconflic = new ArrayList<>();
        alconflic.add(oLending1);

        Mockito.when(userRepo.findByUsername("jeff"))
                .thenReturn(oUser1);
        Mockito.when(userRepo.findByUsername("george"))
                .thenReturn(oUser2);
        Mockito.when(userRepo.findByUsername("franz"))
                .thenReturn(oUser3);
        Mockito.when(userRepo.findUserByuserID(1)).thenReturn(oUser1);
        Mockito.when(userRepo.findUserByuserID(2)).thenReturn(oUser2);
        Mockito.when(userRepo.findUserByuserID(3)).thenReturn(oUser3);
        Mockito.when(lendingRepo.findAllByIsConflict(true)).thenReturn(null);
        Mockito.when(lendingRepo.findLendingBylendingID(7)).thenReturn(oLending1);
        Mockito.when(lendingRepo.save(testLending1)).thenReturn(testLending1);
        Mockito.when(lendingRepo.findLendingBylendingID(8)).thenReturn(oLending2);
        Mockito.when(articleRepo.findById((long) 1)).thenReturn(oArticle);

        p = Mockito.mock(Principal.class);
        Mockito.when(p.getName()).thenReturn("jeff");
    }


    @Test
    public void testAdmin() {
        assertEquals("Admin/admin_conflicthandling", controller.conflictOverview(m, p));
        assertEquals("redirect:/showcase/1", controller.postConflictOverview(1, "show"));
        assertEquals("redirect:/admin/conflicthandling", controller.postConflictOverview(1, "login"));
        assertEquals("Admin/admin_usermanagement", controller.loadUserManagement(m));
        assertEquals("Admin/admin_userEdit", controller.loadEditForm(1, m));
        assertEquals("Admin/admin_userEdit", controller.profileOverview(userRepo.findUserByuserID(1).get(), m, p));
        assertEquals("redirect:/admin/usermanagement/", controller.deleteUser(m, p, 1));
        assertEquals("Admin/admin_createUser", controller.addUser(m));
        assertEquals("Admin/admin_articlemanagement", controller.loadArticleManagement(m));
        assertEquals("redirect:/admin/articlemanagement/", controller.deleteArticle(1, m));
        assertEquals("Admin/admin_lendingmanagement", controller.loadLendingManagement(m));
        // assertEquals("Admin/admin_lendingmanagement",controller.deleteLending(1, m));

    }

    @Test
    public void testUser() {
        Model model = Mockito.mock(Model.class);
        Principal p = Mockito.mock(Principal.class);
        assertEquals("redirect:/login", cController.saveNewUser(model,p,"fritz", "Funkel", "fritzi",
                "Strasse", "22", "40532", "Usebgeren", "DE",
                "test@gmail.com", "123456"));
        assertEquals("redirect:/admin/usermanagement", cController.saveNewUserAsAdmin("fritz", "Funkel", "fritzi",
                "Strasse", "22", "40532", "Usebgeren", "DE",
                "test@gmail.com", "123456", "ROLE_ADMIN "));
        assertEquals("error/usernameExists", cController.validateUsername("fritz"));
    }

    @Test
    public void testEditUser() {
        assertEquals("/EditUser/profileOverview",editUsercontroller.loadEditPage(m,p));
        assertEquals("/EditUser/profileOverview",editUsercontroller.profileOverview(userRepo.findUserByuserID(1).get(),m,p));
    }


}
