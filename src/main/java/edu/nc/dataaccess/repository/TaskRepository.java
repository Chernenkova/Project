package edu.nc.dataaccess.repository;

import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    TaskEntity findByAuthorAndType(User author, String type);
    List<TaskEntity> findAllByMinCostIsLessThanEqualAndTypeIsNotLike(Integer minCost, String type);
    List<TaskEntity> findAll();
}
