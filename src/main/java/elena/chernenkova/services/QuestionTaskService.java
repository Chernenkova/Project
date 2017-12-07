package elena.chernenkova.services;

import elena.chernenkova.common.GeneralSettings;
import elena.chernenkova.entities.TaskEntity;
import elena.chernenkova.repositories.TaskRepository;
import elena.chernenkova.wrappers.questiontask.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
    public ResponseEntity addTask(CreateQuestionTaskWrapper wrapper){
        //TODO: add UUID to question.
        QuestionWrapper[] qArray = new QuestionWrapper[wrapper.getQuestions().length];
        for (int i = 0; i < qArray.length; i++) {
            qArray[i] = new QuestionWrapper(wrapper.getQuestions()[i].getQuestion(),
                    wrapper.getQuestions()[i].getPossibleAnswers());
        }
        QuestionTaskWrapper questionTaskWrapper = new QuestionTaskWrapper(wrapper.getText(),qArray);
        byte[] taskBytes = JsonClassParser.getBytes(questionTaskWrapper);

        QuestionWithAnswerWrapper[] array = wrapper.getQuestions();
        QuestionAnswerWrapper[] qaArray = new QuestionAnswerWrapper[array.length];
        for (int i = 0; i < qaArray.length; i++) {
            qaArray[i] = new QuestionAnswerWrapper(array[i].getQuestion(), array[i].getAnswer());
        }
        byte[] answerBytes = JsonClassParser.getBytes(new QuestionTaskAnswerWrapper(qaArray));

        TaskEntity task = new TaskEntity(GeneralSettings.QUESTION_TASK_TYPE, taskBytes, answerBytes);
        taskRepository.saveAndFlush(task);
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return task and OK status if
     */
    public ResponseEntity<QuestionTaskWrapper> get(Long id){
        TaskEntity entity = taskRepository.getOne(id);
        if(GeneralSettings.QUESTION_TASK_TYPE.equals(entity.getType())){
            QuestionTaskWrapper task = JsonClassParser.getObject(entity.getTask(), QuestionTaskWrapper.class);
            return new ResponseEntity<>(task, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
