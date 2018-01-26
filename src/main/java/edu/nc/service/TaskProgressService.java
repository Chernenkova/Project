package edu.nc.service;


import com.sun.istack.internal.Nullable;
import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.entity.User;
import edu.nc.dataaccess.repository.TaskProgressRepository;
import edu.nc.dataaccess.repository.TaskRepository;
import edu.nc.dataaccess.repository.UserRepository;
import edu.nc.dataaccess.wrapper.taskprogress.TaskInfoWrapper;
import edu.nc.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
public class TaskProgressService {

    private UserRepository userRepository;
    private TaskProgressRepository taskProgressRepository;
    private TaskRepository taskRepository;

    @Autowired
    public TaskProgressService(UserRepository userRepository, TaskProgressRepository taskProgressRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskProgressRepository = taskProgressRepository;
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<TaskInfoWrapper[]> getAvailableAssignments() {
        User user = getCurrentUser();
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<TaskEntity> tasks = taskRepository.findAllByMinCostIsLessThanEqualAndTypeIsNotLike(user.getRaiting(), GeneralSettings.DICTIONARY_TYPE);
        //TODO:
        return new ResponseEntity<>(getFromList(tasks), HttpStatus.OK);
    }


    private @Nullable User getCurrentUser() {
        String login = JwtUserDetails.getUserName();
        if(login == null){
            return null;
        }
        return userRepository.findByUsername(login);
    }

    private TaskInfoWrapper[] getFromList(List<TaskEntity> taskEntityList) {
        TaskInfoWrapper[] array = new TaskInfoWrapper[taskEntityList.size()];
        for (int i = 0; i < array.length; i++) {
            TaskEntity current = taskEntityList.get(i);
            array[i] = new TaskInfoWrapper(current.getName(), current.getType(), current.getReward(), current.getId());
        }
        return array;
    }
}
