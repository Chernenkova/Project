package edu.nc.dataaccess.wrapper.questiontask;

public class QuestionWrapper {

    private String questionUUID;
    private String question;
    private String[] possibleAnswers;

    public QuestionWrapper() {
    }

    public QuestionWrapper(String questionUUID, String question, String[] possibleAnswers) {
        this.questionUUID = questionUUID;
        this.question = question;
        this.possibleAnswers = possibleAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(String[] possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public String getQuestionUUID() {
        return questionUUID;
    }

    public void setQuestionUUID(String questionUUID) {
        this.questionUUID = questionUUID;
    }
}
