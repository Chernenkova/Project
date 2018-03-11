package edu.nc.controller;

import edu.nc.common.GeneralSettings;
import edu.nc.service.WritingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = GeneralSettings.TASK_WRITING)
public class WritingController {

    private WritingService writingService;

    @Autowired
    public WritingController(WritingService writingService) {
        this.writingService = writingService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity getTaskRuss(@PathVariable Long id) {
        return writingService.getTask(id);
    }

}
