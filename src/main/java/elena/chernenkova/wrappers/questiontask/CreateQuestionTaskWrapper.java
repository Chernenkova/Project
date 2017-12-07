package elena.chernenkova.wrappers.questiontask;

public class CreateQuestionTaskWrapper {
    private String text;
    private QuestionWithAnswerWrapper[] questions;

    public CreateQuestionTaskWrapper(String text, QuestionWithAnswerWrapper[] questions) {
        this.text = text;
        this.questions = questions;
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
}
