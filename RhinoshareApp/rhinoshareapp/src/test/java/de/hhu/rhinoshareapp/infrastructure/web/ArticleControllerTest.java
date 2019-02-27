package de.hhu.rhinoshareapp.infrastructure.web;


import de.hhu.rhinoshareapp.controller.article.ArticleController;
import de.hhu.rhinoshareapp.controller.conflict.ConflictController;
import de.hhu.rhinoshareapp.domain.mail.MailService;
import de.hhu.rhinoshareapp.domain.model.Article;
import de.hhu.rhinoshareapp.domain.service.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

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

    @Ignore
    @Test
    public void whenMappingEdit_thenSetNewValuesButKeepOldValuesAsSpecified() throws Exception {
        //Arrange
        Article chainsaw =new  Article("Motorsäge","wie neu",250,25,true,null);
        Article betterChainsaw = new Article("Besser Motorsäge","besser",300,30,false,null);
        Optional<Article> optionalArticle = Optional.of(chainsaw);
        Mockito.when(articleRepo.findById((long)3)).thenReturn(optionalArticle);

        //Act
        mvc.perform(post("/article/edit/3")
                .param("name", betterChainsaw.getName())
                .param("comment", betterChainsaw.getComment())
                .param("deposit", "250")
                .param("rent", "25")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/article/3"))
                .andExpect(redirectedUrl("/article/3"));

        //Assert
        assertEquals(chainsaw.getName(), betterChainsaw.getName());
        assertEquals(chainsaw.getComment(), betterChainsaw.getComment());
        assertEquals(chainsaw.getDeposit(), betterChainsaw.getDeposit());
        assertEquals(chainsaw.getRent(), betterChainsaw.getRent());
    }
}
