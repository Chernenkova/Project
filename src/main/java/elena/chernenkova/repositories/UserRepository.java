package elena.chernenkova.repositories;

import elena.chernenkova.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findFirstByUserId(Long userId);
}