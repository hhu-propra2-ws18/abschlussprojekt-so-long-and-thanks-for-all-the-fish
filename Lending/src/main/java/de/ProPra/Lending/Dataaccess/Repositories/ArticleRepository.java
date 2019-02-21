package de.ProPra.Lending.Dataaccess.Repositories;

import de.ProPra.Lending.Model.Article;
import de.ProPra.Lending.Model.ServiceUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    //Iterable<Article> findAll();
    List<Article> findAllByownerUser(ServiceUser serviceUser);
    Optional<Article> findArticleByarticleID(long id);
    List<Article> findAllArticleByownerUserAndIsRequested(ServiceUser serviceUser, boolean isRequested);
}
