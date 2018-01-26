package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.RecoverEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecoverRepository extends JpaRepository<RecoverEntity, Long> {
    Optional<RecoverEntity> findByUsername(String username);

    Optional<RecoverEntity> findBySecretString(String secretString);
}
