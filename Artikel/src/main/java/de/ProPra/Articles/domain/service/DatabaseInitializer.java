package de.ProPra.Articles.domain.service;

import de.ProPra.Articles.domain.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("Population the Database");
        /*Article one = new Article("dummyname", "dummycomment", 1, 100.0, 10.0, false, null);
        Article two = new Article("dummyname", "dummycomment", 1, 100.0, 10.0, true, null);
        Article three = new Article("dummyname", "dummycomment", 1, 100.0, 10.0, false, null);

        articleRepository.save(one);
        articleRepository.save(two);
        articleRepository.save(three);*/
    }
}
