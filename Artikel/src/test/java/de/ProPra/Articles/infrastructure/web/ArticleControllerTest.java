package de.ProPra.Articles.infrastructure.web;

public class ArticleControllerTest {

    @Autowired
    ArticleRepository articleRepository;

    @Test
    public void testEditArticle() {
        MultipartFile file = new MultipartFile();
        Article article = new Article("Article1", "Art1 Description", 15, 100.0, 10.0, true, file);
        articleRepository.save(article);

        Article newArticle = new Article("Article1", "Art1 new Description", 15, 200.0, 25.0, false, file);

        ArticleController articleController = new ArticleController();

        articleController.editArticlePostMapping(newArticle, 15)

        assertThat(articleRepository.findById(15).getName()).isEqualTo(newArticle.getName());
        assertThat(articleRepository.findById(15).getComment()).isEqualTo(newArticle.getComment());
        assertThat(articleRepository.findById(15).getArticleID()).isEqualTo(newArticle.getArticleID());
        assertThat(articleRepository.findById(15).getDeposit()).isEqualTo(newArticle.getDeposit());
        assertThat(articleRepository.findById(15).getRent()).isEqualTo(newArticle.getRent());
        assertThat(articleRepository.findById(15).isAvailable()).isEqualTo(newArticle.isAvailable());
    }
}