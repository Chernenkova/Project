package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.User;
import edu.nc.security.JwtUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findFirstByUserId(Long userId);

    default Optional<User> getCurrentUser() {
        return Optional.of(findByUsername(JwtUserDetails.getUserName()));
    }
}