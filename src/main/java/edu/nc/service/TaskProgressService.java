package edu.nc.service;


import com.sun.istack.internal.Nullable;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        //TODO: completed tasks
//        tasks = tasks.stream().peek(x -> {
//            Optional<TaskProgressEntity> tpe = completedTasks.stream().filter(y -> y.getTask().getId().equals(x.getId())).findAny();
//            if(tpe.isPresent()) {
//                x.setReward((int) Math.ceil((double)x.getReward() / 10));
//            }
//        }).collect(Collectors.toList());
//        tasks.sort((taskEntity, t1) -> t1.getReward() - taskEntity.getReward());
//
//        return new ResponseEntity<>(getFromList(tasks), HttpStatus.OK);
        return new ResponseEntity<>(getTaskInfo(tasks, completedTasks), HttpStatus.OK);
    }


//    private @Nullable User getCurrentUser() {
//        String login = JwtUserDetails.getUserName();
//        if(login == null){
//            return null;
//        }
//        return userRepository.findByUsername(login);
//    }

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
                reward = (int) Math.ceil((double) x.getReward() / 10);
                completed = true;
            }
            return new TaskInfoWrapper(x.getName(), x.getType(), reward, x.getId(), completed);
        }).sorted((taskEntity, t1) -> t1.getReward() - taskEntity.getReward()).toArray(TaskInfoWrapper[]::new);
    }

    public ResponseEntity completeTask(Long id){
        return completeTask(id, userRepository, taskRepository, taskProgressRepository);
    }

    public static ResponseEntity completeTask(Long id,
                                              UserRepository userRepository,
                                              TaskRepository taskRepository,
                                              TaskProgressRepository taskProgressRepository){
        User current = userRepository.findByUsername(JwtUserDetails.getUserName());
        TaskEntity task = taskRepository.findOne(id);
        if(current == null || task == null){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        List<TaskProgressEntity> list = current.getTasks();
        Optional<TaskProgressEntity> optional = list.stream().filter(entity -> entity.getTask().getId().equals(task.getId())).findAny();
        TaskProgressEntity tpe = null;

        int currentRating = current.getRaiting();
        int increase = 0;
        if(optional.isPresent()){
            tpe = optional.get();
            increase += Math.ceil((double)task.getReward() / 10);

        } else {
            tpe = new TaskProgressEntity(task, TaskProgressStatus.COMPLETED);
            tpe = taskProgressRepository.save(tpe);
            increase += task.getReward();
            current.getTasks().add(tpe);
        }
        if(tpe.getTask().getType().equalsIgnoreCase("CHOOSING")) {
            increase *= tpe.getScore();
            increase = (int) Math.ceil((double)increase / tpe.getTotalScore());
        }
        currentRating += increase;

        current.setRaiting(currentRating);
        current = userRepository.saveAndFlush(current);
        return new ResponseEntity<Integer>(current.getRaiting(), HttpStatus.OK);
    }
}
