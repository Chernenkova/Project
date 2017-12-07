package edu.nc.dataaccess.wrapper.questiontask;

public class QuestionWrapper {
    private String question;
    private String[] possibleAnswers;

    public QuestionWrapper() {
    }

    public QuestionWrapper(String question, String[] possibleAnswers) {
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
}
