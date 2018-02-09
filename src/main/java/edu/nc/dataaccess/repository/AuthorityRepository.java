package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.model.security.Authority;
import edu.nc.dataaccess.model.security.AuthorityName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByName(AuthorityName name);
}
