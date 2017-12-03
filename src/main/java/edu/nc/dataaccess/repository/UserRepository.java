package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findFirstByUserId(Long userId);
}