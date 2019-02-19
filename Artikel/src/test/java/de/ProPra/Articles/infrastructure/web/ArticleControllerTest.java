package de.ProPra.Articles.infrastructure.web;

import de.ProPra.Articles.domain.model.Article;
import de.ProPra.Articles.domain.service.ArticleRepository;
import de.ProPra.Articles.domain.service.ImageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ImageRepository imageRepository;

	@MockBean
	private ArticleRepository articleRepository;

    @Test
    public void whenMappingEdit_thenSetNewValuesButKeepOldValuesAsSpecified() throws Exception {
    	//Arrange
        Article chainsaw = Article.builder().name("Kettensäge").comment("Sägt super").personID(15).articleID(3).deposit(200).rent(15).available(true).build();
		Article betterChainsaw = Article.builder().name("Bessere Kettensäge").comment("Sägt noch besser!").deposit(250).rent(25).available(false).build();
		Mockito.when(articleRepository.findById(3)).thenReturn(chainsaw);

		//Act
		mvc.perform(post("/article/edit/3")

			.sessionAttr("article", betterChainsaw)
			)
				.andExpect(status().isOk())
				.andExpect(view().name("redirect:article/3"))
				.andExpect(redirectedUrl("/article/3"))
				//unchanged values
				.andExpect(model().attribute("id", is("3")))
				.andExpect(model().attribute("personID", is("15")))
				//changed values
				.andExpect(model().attribute("name", is("Bessere Kettensäge")))
				.andExpect(model().attribute("comment", is("Sägt noch besser")))
				.andExpect(model().attribute("deposit", is("250")))
				.andExpect(model().attribute("rent", is("25")))
				.andExpect(model().attribute("available", is("false")));

		//Assert
		assertEquals(chainsaw.getName(), betterChainsaw.getName());
		assertEquals(chainsaw.getComment(), betterChainsaw.getComment());
		assertEquals(chainsaw.getDeposit(), betterChainsaw.getDeposit());
		assertEquals(chainsaw.getRent(), betterChainsaw.getRent());
    }
}