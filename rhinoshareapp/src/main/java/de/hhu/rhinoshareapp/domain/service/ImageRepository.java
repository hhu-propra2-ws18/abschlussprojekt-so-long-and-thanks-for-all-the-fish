package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ImageRepository extends CrudRepository<Image, Long>{
	Image findById(long id);
}
