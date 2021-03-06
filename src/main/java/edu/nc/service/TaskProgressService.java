package edu.nc.service;


import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.entity.TaskProgressEntity;
import edu.nc.dataaccess.entity.TaskProgressStatus;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        Optional<User> opt = userRepository.getCurrentUser();
        if (!opt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = opt.get();
        List<TaskEntity> tasks = taskRepository.findAllByMinCostIsLessThanEqualAndTypeIsNotLike(user.getRaiting(), GeneralSettings.DICTIONARY_TYPE);
        List<TaskProgressEntity> completedTasks = user.getTasks();

        return new ResponseEntity<>(getTaskInfo(tasks, completedTasks), HttpStatus.OK);
    }
    public ResponseEntity<TaskInfoWrapper[]> getAll() {
        Optional<User> opt = userRepository.getCurrentUser();
        if (!opt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //User user = opt.get();
        List<TaskEntity> tasks = taskRepository.findAll();
        //List<TaskProgressEntity> completedTasks = user.getTasks();

        return new ResponseEntity<>(getTaskInfo(tasks, new LinkedList<>()), HttpStatus.OK);
    }

    private TaskInfoWrapper[] getFromList(List<TaskEntity> taskEntityList) {
        TaskInfoWrapper[] array = new TaskInfoWrapper[taskEntityList.size()];
        for (int i = 0; i < array.length; i++) {
            TaskEntity current = taskEntityList.get(i);
            array[i] = new TaskInfoWrapper(current.getName(), current.getType(), current.getReward(), current.getId(), false);
        }
        return array;
    }

    private TaskInfoWrapper[] getTaskInfo(List<TaskEntity> tasks, List<TaskProgressEntity> progress) {
        return tasks.stream().map(x -> {
            Optional<TaskProgressEntity> tpe = progress.stream().filter(y -> y.getTask().getId().equals(x.getId())).findAny();
            int reward = x.getReward();
            boolean completed = false;
            if (tpe.isPresent()) {
                if (GeneralSettings.WRITING_TYPE.equals(tpe.get().getTask().getType())) {
                    reward = (int) Math.ceil((double) x.getReward() / 10);
                } else {
                    reward = 0;
                }
                completed = true;
            }
            return new TaskInfoWrapper(x.getName(), x.getType(), reward, x.getId(), completed);
        }).sorted((taskEntity, t1) -> t1.getReward() - taskEntity.getReward()).toArray(TaskInfoWrapper[]::new);
    }

    public ResponseEntity completeTask(Long id) {
        return completeTask(id, userRepository, taskRepository, taskProgressRepository);
    }

    public static ResponseEntity completeTask(Long id,
                                              UserRepository userRepository,
                                              TaskRepository taskRepository,
                                              TaskProgressRepository taskProgressRepository) {
        User current = userRepository.findByUsername(JwtUserDetails.getUserName());
        TaskEntity task = taskRepository.findOne(id);
        if (current == null || task == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        List<TaskProgressEntity> list = current.getTasks();
        Optional<TaskProgressEntity> optional = list.stream().filter(entity -> entity.getTask().getId().equals(task.getId())).findAny();
        TaskProgressEntity tpe = null;

        int currentRating = current.getRaiting();
        int increase = 0;

        if (!optional.isPresent()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        tpe = optional.get();
        if (tpe.getStatus() == TaskProgressStatus.FIRST) {
            increase = tpe.getTask().getReward();
        } else if (GeneralSettings.WRITING_TYPE.equals(tpe.getTask().getType())) {
            increase = (int) Math.ceil((double) task.getReward() / 10);
        }
        if (GeneralSettings.CHOOSING_TASK_BASIC_TYPE.equals(tpe.getTask().getType()) ||
                GeneralSettings.VIDEO_TASK_TYPE.equals(tpe.getTask().getType()) ||
                GeneralSettings.GRAMMAR_TASK_TYPE.equals(tpe.getTask().getType()) ||
                GeneralSettings.QUESTION_TASK_TYPE.equals(tpe.getTask().getType())) {
            increase *= tpe.getScore();
            increase = (int) Math.ceil((double) increase / tpe.getTotalScore());
        }
        tpe.setStatus(TaskProgressStatus.COMPLETED);
        tpe = taskProgressRepository.saveAndFlush(tpe);
        currentRating += increase;

        current.setRaiting(currentRating);
        current = userRepository.saveAndFlush(current);
        return new ResponseEntity<Integer>(current.getRaiting(), HttpStatus.OK);
    }
}
