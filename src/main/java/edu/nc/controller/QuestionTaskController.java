package edu.nc.controller;


import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.wrapper.questiontask.*;
import edu.nc.service.QuestionTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = GeneralSettings.QUESTION_TASK)
public class QuestionTaskController {

    private static final String CREATE_QUESTION = "create";
    private static final String CREATE_VIDEO = "create-video";
    private static final String CREATE_GRAMMAR = "create-grammar";

    //TODO: unify methods
    private QuestionTaskService questionTaskService;

    @Autowired
    public QuestionTaskController(QuestionTaskService questionTaskService) {
        this.questionTaskService = questionTaskService;
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.POST)
    public ResponseEntity addTask(@RequestBody CreateQuestionTaskWrapper wrapper,
                                  @PathVariable("type") String type) {
        switch (type) {
            case CREATE_QUESTION: {
                return questionTaskService.addTask(wrapper, GeneralSettings.QUESTION_TASK_TYPE);
            }
            case CREATE_VIDEO: {
                return questionTaskService.addTask(wrapper, GeneralSettings.VIDEO_TASK_TYPE);
            }
            case CREATE_GRAMMAR: {
                return questionTaskService.addTask(wrapper, GeneralSettings.GRAMMAR_TASK_TYPE);
            }
            default: {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }

    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public ResponseEntity<QuestionTaskWrapper> get(@PathVariable Long id) {
        return questionTaskService.get(id);
    }

    @RequestMapping(value = "/videoTask/{id}", method = RequestMethod.GET)
    public ResponseEntity<QuestionTaskWrapper> getVideo(@PathVariable Long id) {
        return questionTaskService.getVideo(id);
    }

    @RequestMapping(value = "/check/{id}")
    public ResponseEntity<QuestionResult> check(@PathVariable("id") Long id,
                                                @RequestBody QuestionTaskAnswerWrapper wrapper) {
        return questionTaskService.check(id, wrapper);
    }

    @RequestMapping(value = "/checkVideo/{id}")
    public ResponseEntity<QuestionResult> checkVideo(@PathVariable("id") Long id,
                                                     @RequestBody QuestionTaskAnswerWrapper wrapper) {
        return questionTaskService.checkVideo(id, wrapper);
    }

}
