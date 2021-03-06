package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.RegUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegUserEntity, Long> {
    Optional<RegUserEntity> findByLogin(String login);

    Optional<RegUserEntity> findByUuid(String uuid);
}
