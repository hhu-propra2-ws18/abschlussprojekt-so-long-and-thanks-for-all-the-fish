package de.ProPra.Articles.domain.service;

import de.ProPra.Articles.domain.model.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
}
