package edu.nc.controller;

import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.service.ChoosingTranslationTaskService;
import edu.nc.dataaccess.wrapper.cardtask.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = GeneralSettings.CHOOSING_TRANSLATION_TASK)
public class ChoosingTranslationTaskController {


    private ChoosingTranslationTaskService service;

    @Autowired
    public ChoosingTranslationTaskController(ChoosingTranslationTaskService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TaskEntity> addTask(@RequestBody ChoosingTranslationTaskWrapper wrapper) {
        return service.addTask(wrapper);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/task")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<ChoosingTranslationTaskWrapper> get(@PathVariable Long id) {
        return service.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/task/{id}")
    public ResponseEntity<ChoosingTranslationWrapper> getTask(@PathVariable Long id) {
        return service.getTask(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/task/result/{id}")
    public ResponseEntity<CardResponseWrapper> checkTranslation(@RequestBody CardWrapperIdAndTranslation wrapper, @PathVariable Long id) {
        return service.checkTranslation(wrapper, id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create/basic")
    public ResponseEntity createBasicTask(@RequestBody BasicTaskWrapper wrapper) {
        return service.createBasicTask(wrapper);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResponseEntity create(@RequestBody SetWrapper setWrapper) {
        return service.create(setWrapper);
    }

}
