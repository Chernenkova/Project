package edu.nc.dataaccess.wrapper.questiontask;

public class QuestionTaskWrapper {
    private String name;
    private String text;
    private QuestionWrapper[] questions;

    public QuestionTaskWrapper() {
    }

    public QuestionTaskWrapper(String name, String text, QuestionWrapper[] questions) {
        this.text = text;
        this.questions = questions;
        this.name = name;
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

    public QuestionWrapper[] getQuestions() {
        return questions;
    }

    public void setQuestions(QuestionWrapper[] questions) {
        this.questions = questions;
    }
}
