package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.TaskProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskProgressRepository extends JpaRepository<TaskProgressEntity, Long> {
}
