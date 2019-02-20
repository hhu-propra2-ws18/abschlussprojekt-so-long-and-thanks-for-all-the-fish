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

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
	public void whenMappingSearch_thenReturnAllArticlesWithQueryInNameOrComment() throws Exception {
    	//Arrange
		Article chainsaw = Article.builder().name("Kettensäge").comment("Super Teil!").build();
		Article chippers = Article.builder().name("Häcksler").comment("Macht alles klein, wie eine Säge!").build();
		Article saw = Article.builder().name("Säge").comment("Meine liebste Säge!").build();
		Article plow = Article.builder().name("Schneepflug").comment("Befreit deine Straße von Schnee!").build();
		//persist all 4, query should return only the first 3

		//Act
		mvc.perform(get("/article/search")
				.param("query", "säge")
			)
				.andExpect(status().isOk())
				.andExpect(view().name("searchArticle"))
				.andExpect(model().attributeExists("articles"));

		//Assert

	}
}