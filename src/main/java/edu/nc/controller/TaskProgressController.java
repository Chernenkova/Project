package edu.nc.controller;


import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.wrapper.taskprogress.TaskInfoWrapper;
import edu.nc.service.TaskProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = GeneralSettings.TASK_PROGRESS)
public class TaskProgressController {

    private TaskProgressService taskProgressService;

    @Autowired
    public TaskProgressController(TaskProgressService taskProgressService) {
        this.taskProgressService = taskProgressService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<TaskInfoWrapper[]> getAvailableAssignments() {
        return taskProgressService.getAvailableAssignments();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity<TaskInfoWrapper[]> getAll(){
        return taskProgressService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public ResponseEntity completeTask(@PathVariable Long id) {
        return taskProgressService.completeTask(id);
    }
}
