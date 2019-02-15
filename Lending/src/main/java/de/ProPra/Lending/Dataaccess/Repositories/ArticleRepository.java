package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.Lending;
import de.ProPra.Lending.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    Iterable<Article> findAll();
    List<Article> findAllByownerUser(User user);
    Optional<Article> findArticleByarticleID(long id);
    List<Article> findAllArticleByownerUserAndIsRequested(User user, boolean isRequested);
}
