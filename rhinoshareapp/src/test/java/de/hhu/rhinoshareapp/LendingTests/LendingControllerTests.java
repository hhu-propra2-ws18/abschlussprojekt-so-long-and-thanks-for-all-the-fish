package de.hhu.rhinoshareapp.LendingTests;

import de.hhu.rhinoshareapp.Representations.LendingProcessor.APIProcessor;
import de.hhu.rhinoshareapp.Representations.LendingProcessor.PostProccessor;
import de.hhu.rhinoshareapp.controller.lendings.LendingController;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.model.Lending;
import de.hhu.rhinoshareapp.domain.model.Person;
import de.hhu.rhinoshareapp.domain.service.*;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
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
import java.util.Calendar;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@WebMvcTest
@SuppressFBWarnings // gemockte Repositories werden für Controller tests benötigt
public class LendingControllerTests {
    @Autowired
    LendingController controller;

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

    @MockBean
    ChatMessageRepository chatRepo;

    @Mock
    Principal p;

    @MockBean
    PostProccessor postProccessor;

    @MockBean
    APIProcessor apiProcessor;

    @Before
    public void setUp() {
        Person testPerson1 = new Person("Jeff", "Nosbusch", null, "jeff", "jeff@mail.com", "1234", "user");
        Person testPerson2 = new Person("George", "Pi", null, "george", "george@mail.com", "1234", "user");
        Person testPerson3 = new Person("Franz", "Hoff", null, "franz", "franz@mail.com", "1234", "user");

        Article testArticle1 = new Article("Rasenmäher", "funktioniert, kein Benzin, Schnitthöhe 1cm - 50 m", 500, 25, true, null);
        Article testArticle2 = new Article("Geschirr", "nur ein bisschen zerbrochen, für 20 mann", 250, 25, true, null);
        Article testArticle3 = new Article("Grillkohle", "schon verbrannt", 25230, 88, false, null);

        userRepo.save(testPerson1);
        userRepo.save(testPerson2);
        userRepo.save(testPerson3);
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

        Lending testLending1 = new Lending(date1, date2, testPerson1, testArticle2);
        Lending testLending2 = new Lending(date3, date4, testPerson2, testArticle1);
        testLending2.setConflict(true);
        testLending1.getLendedArticle().setOwner(testPerson3);
        lendingRepo.save(testLending1);
        lendingRepo.save(testLending2);

        Optional<Person> oUser1 = Optional.of(testPerson1);
        Optional<Person> oUser2 = Optional.of(testPerson2);
        Optional<Person> oUser3 = Optional.of(testPerson3);
        Optional<Lending> oLending1 = Optional.of(testLending1);
        Optional<Lending> oLending2 = Optional.of(testLending2);

        Mockito.when(userRepo.findByUsername("jeff"))
                .thenReturn(oUser1);
        Mockito.when(userRepo.findByUsername("george"))
                .thenReturn(oUser2);
        Mockito.when(userRepo.findByUsername("franz"))
                .thenReturn(oUser3);
        Mockito.when(userRepo.findUserByuserID(1)).thenReturn(oUser1);
        Mockito.when(userRepo.findUserByuserID(2)).thenReturn(oUser2);
        Mockito.when(userRepo.findUserByuserID(0)).thenReturn(oUser3);
        Mockito.when(lendingRepo.findAllByIsConflict(true)).thenReturn(null);
        Mockito.when(lendingRepo.findLendingBylendingID(7)).thenReturn(oLending1);
        Mockito.when(lendingRepo.save(testLending1)).thenReturn(testLending1);
        Mockito.when(lendingRepo.findLendingBylendingID(8)).thenReturn(oLending2);

        p = Mockito.mock(Principal.class);
        Mockito.when(p.getName()).thenReturn("jeff");

        Mockito.when(postProccessor.findUserIDByUser(userRepo, "jeff")).thenReturn((long) 1);


    }


    @Test
    public void testOverview() {
        assertEquals("Lending/overview", controller.overview(m, p));
    }

    @Test
    public void testLending() {
        assertEquals("Lending/overviewLendings", controller.lendingPage(m, p));
    }

    @Test
    public void testBorrow() {
        assertEquals("Lending/overviewBorrows", controller.borrowPage(m, p));
    }

    @Test
    public void testConflict() {
        assertEquals("Lending/conflictPage", controller.releaseConflictingLending(m, p));
    }

    @Test
    public void testShowTransactionst() {
        assertEquals("Lending/transactionsPage", controller.showTransactions(m, p));
    }


}
