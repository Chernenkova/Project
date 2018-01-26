package edu.nc.dataaccess.wrapper.questiontask;

public class QuestionWithAnswerWrapper{
    private String answer;
    private String question;
    private String[] possibleAnswers;

    public QuestionWithAnswerWrapper() {
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
