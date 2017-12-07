package edu.nc.controller;


import edu.nc.common.GeneralSettings;
import edu.nc.service.QuestionTaskService;
import edu.nc.dataaccess.wrapper.questiontask.CreateQuestionTaskWrapper;
import edu.nc.dataaccess.wrapper.questiontask.QuestionTaskWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = GeneralSettings.QUESTION_TASK)
public class QuestionTaskController {

    private QuestionTaskService questionTaskService;

    @Autowired
    public QuestionTaskController(QuestionTaskService questionTaskService) {
        this.questionTaskService = questionTaskService;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity addTask(@RequestBody CreateQuestionTaskWrapper wrapper){
        return questionTaskService.addTask(wrapper);
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public ResponseEntity<QuestionTaskWrapper> get(@PathVariable Long id){
        return questionTaskService.get(id);
    }

}
