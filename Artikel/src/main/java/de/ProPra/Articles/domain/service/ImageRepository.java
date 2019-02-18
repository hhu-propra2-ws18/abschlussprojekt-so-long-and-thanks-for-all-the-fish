package de.ProPra.Articles.domain.service;

import de.ProPra.Articles.domain.model.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long>{
}
