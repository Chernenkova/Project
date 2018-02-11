package edu.nc.service;


import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.repository.TaskRepository;
import edu.nc.dataaccess.wrapper.questiontask.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class QuestionTaskService {


    private TaskRepository taskRepository;

    @Autowired
    public QuestionTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Creates TaskEntity and makes target {@link GeneralSettings} QUESTION_TASK_TYPE
     * @see TaskEntity
     * @param wrapper - contains text, array: question, possible answers and correct answer
     * @return OK status
     */
    public ResponseEntity addTask(CreateQuestionTaskWrapper wrapper, String type){
                QuestionWrapper[] qArray = new QuestionWrapper[wrapper.getQuestions().length];
        for (int i = 0; i < qArray.length; i++) {
            qArray[i] = new QuestionWrapper(UUID.randomUUID().toString(), wrapper.getQuestions()[i].getQuestion(),
                    wrapper.getQuestions()[i].getPossibleAnswers());
        }
        QuestionTaskWrapper questionTaskWrapper = new QuestionTaskWrapper(wrapper.getText(),qArray);
        byte[] taskBytes = JsonClassParser.getBytes(questionTaskWrapper);

        QuestionWithAnswerWrapper[] array = wrapper.getQuestions();
        QuestionAnswerWrapper[] qaArray = new QuestionAnswerWrapper[array.length];
        for (int i = 0; i < qaArray.length; i++) {
            qaArray[i] = new QuestionAnswerWrapper(qArray[i].getQuestionUUID(), array[i].getAnswer());
        }
        byte[] answerBytes = JsonClassParser.getBytes(new QuestionTaskAnswerWrapper(qaArray));

        TaskEntity task = new TaskEntity(type, taskBytes, answerBytes,null,
                "",wrapper.getReward() * wrapper.getQuestions().length,0);
        taskRepository.saveAndFlush(task);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     * @param id    - task ID in DB
     * @return task and OK status if there is task in DB
     *                  NOT_FOUND if there is no task in DB
     *                  BAD_REQUEST if task has wrong TYPE
     */
    public ResponseEntity<QuestionTaskWrapper> get(Long id){
        TaskEntity entity = taskRepository.findOne(id);
        if(null == entity){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(GeneralSettings.QUESTION_TASK_TYPE.equals(entity.getType())){
            QuestionTaskWrapper task = JsonClassParser.getObject(entity.getTask(), QuestionTaskWrapper.class);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    /**
     * @param id    - task ID in DB
     * @return task and OK status if there is task in DB
     *                  NOT_FOUND if there is no task in DB
     *                  BAD_REQUEST if task has wrong TYPE
     */
    public ResponseEntity<QuestionTaskWrapper> getVideo(Long id){
        TaskEntity entity = taskRepository.findOne(id);
        if(null == entity){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(GeneralSettings.VIDEO_TASK_TYPE.equals(entity.getType())){
            QuestionTaskWrapper task = JsonClassParser.getObject(entity.getTask(), QuestionTaskWrapper.class);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    /**
     * compare user's answers with ones in DB
     * @param id        - task ID in DB
     * @param wrapper   - contains questions UUIDs and user's chosen answers (text)
     * @return wrapper, which contains  -totalScore (max possible for task)
     *                                  -score (quantity of correct answers)
     *                                  -correct answers (question UUID amd answer)
     *
     *                  OK status if there is task in DB
     *                  NOT_FOUND if there is no task in DB
     *                  BAD_REQUEST if task has wrong TYPE
     */
    public ResponseEntity<QuestionResult> check(Long id, QuestionTaskAnswerWrapper wrapper){
        //get task from DB
        TaskEntity entity = taskRepository.findOne(id);
        if(entity == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!entity.getType().equalsIgnoreCase(GeneralSettings.QUESTION_TASK_TYPE)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        QuestionTaskAnswerWrapper correct = JsonClassParser.getObject(entity.getAnswer(), QuestionTaskAnswerWrapper.class);

        Map<String, String> correctMap = new HashMap<>();
        for (int i = 0; i < correct.getQa().length; i++) {
            correctMap.put( correct.getQa()[i].getQuestionUUID(),
                            correct.getQa()[i].getAnswer());
        }
        //response processing
        int totalScore = correctMap.size();
        int score = 0;
        for (int i = 0; i < wrapper.getQa().length; i++) {
            QuestionAnswerWrapper current = wrapper.getQa()[i];
            if(current.getAnswer()!= null && current.getAnswer().equalsIgnoreCase(correctMap.get(current.getQuestionUUID()))){
                score++;
            }
        }
        //TODO: update data in DB
        QuestionResult result = new QuestionResult(totalScore, score, correct);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
    /**
     * compare user's answers with ones in DB
     * @param id        - task ID in DB
     * @param wrapper   - contains questions UUIDs and user's chosen answers (text)
     * @return wrapper, which contains  -totalScore (max possible for task)
     *                                  -score (quantity of correct answers)
     *                                  -correct answers (question UUID amd answer)
     *
     *                  OK status if there is task in DB
     *                  NOT_FOUND if there is no task in DB
     *                  BAD_REQUEST if task has wrong TYPE
     */
    public ResponseEntity<QuestionResult> checkVideo(Long id, QuestionTaskAnswerWrapper wrapper){
        //get task from DB
        TaskEntity entity = taskRepository.findOne(id);
        if(entity == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!entity.getType().equalsIgnoreCase(GeneralSettings.VIDEO_TASK_TYPE)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        QuestionTaskAnswerWrapper correct = JsonClassParser.getObject(entity.getAnswer(), QuestionTaskAnswerWrapper.class);

        Map<String, String> correctMap = new HashMap<>();
        for (int i = 0; i < correct.getQa().length; i++) {
            correctMap.put( correct.getQa()[i].getQuestionUUID(),
                    correct.getQa()[i].getAnswer());
        }
        //response processing
        int totalScore = correctMap.size();
        int score = 0;
        for (int i = 0; i < wrapper.getQa().length; i++) {
            QuestionAnswerWrapper current = wrapper.getQa()[i];
            if(current.getAnswer()!= null && current.getAnswer().equalsIgnoreCase(correctMap.get(current.getQuestionUUID()))){
                score++;
            }
        }
        //TODO: update data in DB
        QuestionResult result = new QuestionResult(totalScore, score, correct);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
