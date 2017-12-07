package edu.nc.dataaccess.wrapper.questiontask;

public class QuestionAnswerWrapper {
    private String question;
    private String answer;

    public QuestionAnswerWrapper(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public QuestionAnswerWrapper() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
