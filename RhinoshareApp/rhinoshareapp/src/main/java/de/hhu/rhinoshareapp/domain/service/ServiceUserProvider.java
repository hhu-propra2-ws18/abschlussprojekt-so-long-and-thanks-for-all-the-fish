package de.hhu.rhinoshareapp.domain.service;

import de.hhu.rhinoshareapp.domain.model.ServiceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceUserProvider extends JpaRepository<ServiceUser, Long> {

    Optional<ServiceUser> findByUsername(String username);
}
