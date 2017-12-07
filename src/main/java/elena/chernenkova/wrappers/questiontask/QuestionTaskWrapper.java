package elena.chernenkova.wrappers.questiontask;

public class QuestionTaskWrapper {
    private String text;
    private QuestionWrapper[] questions;

    public QuestionTaskWrapper() {
    }

    public QuestionTaskWrapper(String text, QuestionWrapper[] questions) {
        this.text = text;
        this.questions = questions;
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
