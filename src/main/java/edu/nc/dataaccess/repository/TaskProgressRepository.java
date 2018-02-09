package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.entity.TaskProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskProgressRepository extends JpaRepository<TaskProgressEntity, Long> {
}
