package edu.nc.dataaccess.wrapper.questiontask;

public class CreateQuestionTaskWrapper {
    private String text;
    private QuestionWithAnswerWrapper[] questions;
    private Integer reward;

    public CreateQuestionTaskWrapper(String text, QuestionWithAnswerWrapper[] questions, Integer reward) {
        this.text = text;
        this.questions = questions;
        this.reward = reward;
    }

    public CreateQuestionTaskWrapper() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionWithAnswerWrapper[] getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionWithAnswerWrapper[] questions) {
        this.questions = questions;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }
}
