package edu.nc.dataaccess.wrapper.questiontask;

public class CreateQuestionTaskWrapper {
    private String name;
    private String text;
    private QuestionWithAnswerWrapper[] questions;
    private Integer reward;
    private Integer minCost;
    private Long authorId;

    public CreateQuestionTaskWrapper(String name, String text, QuestionWithAnswerWrapper[] questions, Integer reward,
                                     Integer minCost, String authorId) {
        this.name = name;
        this.text = text;
        this.questions = questions;
        this.reward = reward;
        this.minCost = minCost;
        this.authorId = Long.parseLong(authorId);
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getMinCost() {
        return minCost;
    }

    public void setMinCost(Integer minCost) {
        this.minCost = minCost;
    }

    public CreateQuestionTaskWrapper() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
