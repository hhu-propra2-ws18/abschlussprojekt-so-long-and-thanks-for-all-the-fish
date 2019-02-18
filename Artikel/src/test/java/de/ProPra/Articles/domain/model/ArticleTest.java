package de.ProPra.Articles.domain.model;

import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ArticleTest {

    @Test
    public void testCreateArticle() throws IOException, SQLException {
        MultipartFile file = null;
        Article article = new Article("Article1", "Art1 Description", 15, 100, 10, true, file);

        assertEquals(article.getName(), "Article1");
        assertEquals(article.getComment(), "Art1 Description");
        assertEquals(article.getPersonID(), 15);
        assertEquals(article.getDeposit(), 100);
        assertEquals(article.getRent(), 10);
        assertTrue(article.isAvailable());
    }
}