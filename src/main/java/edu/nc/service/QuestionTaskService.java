package edu.nc.service;


import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.entity.TaskEntity;
import edu.nc.dataaccess.entity.TaskProgressEntity;
import edu.nc.dataaccess.entity.TaskProgressStatus;
import edu.nc.dataaccess.entity.User;
import edu.nc.dataaccess.repository.TaskProgressRepository;
import edu.nc.dataaccess.repository.TaskRepository;
import edu.nc.dataaccess.repository.UserRepository;
import edu.nc.dataaccess.serializerwrappers.ChoosingTranslationTaskSerializerWrapper;
import edu.nc.dataaccess.wrapper.questiontask.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuestionTaskService {


    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private TaskProgressRepository taskProgressRepository;
    @Autowired
    public QuestionTaskService(TaskRepository taskRepository, UserRepository userRepository, TaskProgressRepository taskProgressRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskProgressRepository = taskProgressRepository;
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
        if(GeneralSettings.QUESTION_TASK_TYPE.equals(entity.getType()) ||
                //TODO: recreate
                GeneralSettings.GRAMMAR_TASK_TYPE.equals(entity.getType())){


            QuestionTaskWrapper task = JsonClassParser.getObject(entity.getTask(), QuestionTaskWrapper.class);
            addToDbInfoStart(id);
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
            addToDbInfoStart(id);
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
        if(!(entity.getType().equalsIgnoreCase(GeneralSettings.QUESTION_TASK_TYPE)
                || entity.getType().equalsIgnoreCase(GeneralSettings.GRAMMAR_TASK_TYPE))){
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
        User current = addToDbInfoCheck(id, totalScore, score);
        if(current == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        QuestionResult result = new QuestionResult(totalScore, score, correct, current.getRaiting());
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
        User current = addToDbInfoCheck(id, totalScore, score);
        if(current == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        QuestionResult result = new QuestionResult(totalScore, score, correct, current.getRaiting());
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    private User addToDbInfoCheck(Long id, int totalScore, int score) {
        Optional<User> optUser = userRepository.getCurrentUser();
        if(!optUser.isPresent()){
            return null;
        }
        User current = optUser.get();
        TaskEntity taskEntity = taskRepository.findOne(id);
        if (taskEntity == null) {
            return null;
        }
        Optional<TaskProgressEntity> optTpe= current.getTasks().stream().filter(x -> x.getTask().getId().equals(id)).findAny();
        if(!optTpe.isPresent()){
            return null;
        }
        TaskProgressEntity tpe = optTpe.get();
        tpe.setScore(score);
        tpe.setTotalScore(totalScore);
        if(tpe.getStatus() == TaskProgressStatus.FIRST){
            current.setRaiting(current.getRaiting() + (taskEntity.getReward() * score / totalScore));
            current = userRepository.saveAndFlush(current);
        }
        tpe.setStatus(TaskProgressStatus.COMPLETED);

        tpe = taskProgressRepository.saveAndFlush(tpe);
        return current;
    }

    private TaskProgressEntity addToDbInfoStart(Long id){
        Optional<User> optUser = userRepository.getCurrentUser();
        if(!optUser.isPresent()){
            return null;
        }
        User current = optUser.get();
        TaskEntity task = taskRepository.findOne(id);
        if (task == null) {
            return null;
        }
        QuestionTaskAnswerWrapper c1 = JsonClassParser.getObject(task.getAnswer(),
                QuestionTaskAnswerWrapper.class);

        Optional<TaskProgressEntity> optionalTaskEntity = current.getTasks()
                .stream()
                .filter(x -> x.getTask().getId().equals(id))
                .findAny();

        TaskProgressEntity tpe = null;
        if(optionalTaskEntity.isPresent()){
            //User has already executed this task
            tpe = optionalTaskEntity.get();
            tpe.setStatus(TaskProgressStatus.IN_PROGRESS);

        }else {
            //User has not executed this task yet
            tpe = new TaskProgressEntity(task, TaskProgressStatus.FIRST);
        }
        tpe.setScore(0);
        tpe.setTotalScore(c1.getQa().length);
        tpe = taskProgressRepository.saveAndFlush(tpe);
        if(!optionalTaskEntity.isPresent()){
            current.getTasks().add(tpe);
        }
        current = userRepository.saveAndFlush(current);
        return tpe;
    }
}
