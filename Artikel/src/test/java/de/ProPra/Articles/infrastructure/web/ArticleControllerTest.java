package de.ProPra.Articles.infrastructure.web;

import de.ProPra.Articles.domain.model.Article;
import de.ProPra.Articles.domain.service.ArticleRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ArticleControllerTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void testEditArticle() throws IOException, SQLException {
        MultipartFile file = null;
        Article article = new Article("Article1", "Art1 Description", 15, 100, 10, true, file);
        ArticleController articleController = new ArticleController();

        Article newArticle = new Article("Article1", "Art1 new Description", 15, 200, 25, false, file);
        articleController.editArticlePostMapping(newArticle, (long) 15);

        assertEquals(article.getName(),newArticle.getName());
        assertEquals(article.getComment(),newArticle.getComment());
        assertEquals(article.getArticleID(),newArticle.getArticleID());
        assertEquals(article.getDeposit(),newArticle.getDeposit());
        assertEquals(article.getRent(),newArticle.getRent());
        assertEquals(article.isAvailable(), newArticle.isAvailable());

    }
}