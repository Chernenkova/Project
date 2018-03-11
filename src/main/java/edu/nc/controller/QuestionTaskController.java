package edu.nc.controller;


import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.wrapper.questiontask.*;
import edu.nc.security.JwtUser;
import edu.nc.security.JwtUserDetails;
import edu.nc.service.QuestionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        return questionTaskService.addTask(wrapper, GeneralSettings.QUESTION_TASK_TYPE);
    }

    @RequestMapping(value = "/create-video", method = RequestMethod.POST)
    public ResponseEntity addTaskVideo(@RequestBody CreateQuestionTaskWrapper wrapper){
        return questionTaskService.addTask(wrapper, GeneralSettings.VIDEO_TASK_TYPE);
    }
    @RequestMapping(value = "/create-grammar", method = RequestMethod.POST)
    public ResponseEntity addTaskGrammar(@RequestBody CreateQuestionTaskWrapper wrapper){
        return questionTaskService.addTask(wrapper, GeneralSettings.GRAMMAR_TASK_TYPE);
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public ResponseEntity<QuestionTaskWrapper> get(@PathVariable Long id){
        return questionTaskService.get(id);
    }

    @RequestMapping(value = "/videoTask/{id}", method = RequestMethod.GET)
    public ResponseEntity<QuestionTaskWrapper> getVideo(@PathVariable Long id){
        return questionTaskService.getVideo(id);
    }

    //TODO: create
    @RequestMapping(value = "/check/{id}")
    public ResponseEntity<QuestionResult> check(@PathVariable("id") Long id,
                                                @RequestBody QuestionTaskAnswerWrapper wrapper){
        return questionTaskService.check(id, wrapper);
    }

    @RequestMapping(value = "/checkVideo/{id}")
    public ResponseEntity<QuestionResult> checkVideo(@PathVariable("id") Long id,
                                                @RequestBody QuestionTaskAnswerWrapper wrapper){
        return questionTaskService.checkVideo(id, wrapper);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id){
        System.out.println("!!!" + id);
        return questionTaskService.delete(id);
    }

}
