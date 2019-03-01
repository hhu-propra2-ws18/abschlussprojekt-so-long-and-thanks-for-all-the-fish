package de.hhu.rhinoshareapp.errorMappingTests;

import de.hhu.rhinoshareapp.controller.ErrorMappingController;
import de.hhu.rhinoshareapp.controller.LoginPageController;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@WebMvcTest
public class ErrorMappingTests {
    @Autowired
    ErrorMappingController controller;

    @Autowired
    LoginPageController c2;

    @Autowired
    MockMvc mvc;

    @MockBean
    Model m;

    @MockBean
    LendingRepository lendingRepo;

    @MockBean
    ChatMessageRepository chatMessageRepository;

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
    HttpServletRequest http;


    @Test
    public void testErrorMapping() throws Exception {
        Mockito.when(http.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.FORBIDDEN.value());
        assertEquals("error/403", controller.handleError(http));
        Mockito.when(http.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.NOT_FOUND.value());
        assertEquals("error/404", controller.handleError(http));
        Mockito.when(http.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertEquals("error/500", controller.handleError(http));

    }

    @Test
    public void testAynError() throws Exception {
        assertEquals("/error", controller.getErrorPath());
    }

    @Test
    public  void testLogin(){
        assertEquals("loginpage",c2.loadPage());
        assertEquals("createuser",c2.loadNewAccountPage());
    }

}