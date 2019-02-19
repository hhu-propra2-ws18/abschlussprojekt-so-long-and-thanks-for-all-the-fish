package de.ProPra.Articles.infrastructure.web;

import de.ProPra.Articles.domain.model.Article;
import de.ProPra.Articles.domain.service.ArticleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ArticleRepository articleRepository;

    @Test
    public void whenMappingEdit_thenSetNewValuesButKeepOldValuesAsSpecified() throws Exception {
    	//Arrange
        Article chainsaw = Article.builder().name("Kettensäge").comment("Sägt super").personID(15).articleID(3).deposit(200).rent(15).available(true).build();
		Article betterChainsaw = Article.builder().name("Bessere Kettensäge").comment("Sägt noch besser!").deposit(250).rent(25).available(false).build();

		//Act
		mvc.perform(post("/article/edit/3"))
				.andExpect(status().isOk())
				.andExpect()
		;


    }
}