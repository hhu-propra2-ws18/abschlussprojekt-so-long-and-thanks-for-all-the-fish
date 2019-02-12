package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import org.springframework.data.repository.CrudRepository;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    Iterable<Article> findAll();
}
