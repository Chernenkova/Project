package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    TaskEntity findByAuthorAndType(User author, String type);
}
