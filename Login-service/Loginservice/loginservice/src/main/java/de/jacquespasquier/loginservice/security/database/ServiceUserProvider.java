package de.jacquespasquier.loginservice.security.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceUserProvider extends JpaRepository<ServiceUser, Long> {

    Optional<ServiceUser> findByUsername(String username);
}
