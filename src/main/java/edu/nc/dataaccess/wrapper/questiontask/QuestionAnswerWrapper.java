package edu.nc.dataaccess.wrapper.questiontask;

public class QuestionAnswerWrapper {
    private String questionUUID;
    private String answer;

    public QuestionAnswerWrapper() {
    }

    public QuestionAnswerWrapper(String questionUUID, String answer) {
        this.questionUUID = questionUUID;
        this.answer = answer;
    }

    public String getQuestionUUID() {
        return questionUUID;
    }

    public void setQuestionUUID(String questionUUID) {
        this.questionUUID = questionUUID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
