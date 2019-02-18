package de.ProPra.Articles.domain.model

public class ArticleTest {

    @Test
    public void testCreateArticle() {
        MultipartFile file = new MultipartFile();
        Article article = new Article("Article1", "Art1 Description", 15, 100.0, 10.0, true, file);

        assertEquals(article.getName(), "Article1");
        assertEquals(article.getDescription(), "Art1 Description");
        assertEquals(article.getPersonID(), 15);
        assertEquals(article.getDeposit(), 100.0);
        assertEquals(article.getRent(), 10.0);
        assertEquals(article.isAvailable(), true);
    }
}