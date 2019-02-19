package de.ProPra.Articles.domain.service;

import de.ProPra.Articles.domain.model.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    List<Article> findAllByNameContainingOrCommentContainingAllIgnoreCase(String namequery, String commentquery);
}
