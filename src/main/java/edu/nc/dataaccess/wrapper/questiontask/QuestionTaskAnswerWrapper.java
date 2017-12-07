package elena.chernenkova.wrappers.questiontask;

public class QuestionTaskAnswerWrapper {
    private QuestionAnswerWrapper[] qa;

    public QuestionTaskAnswerWrapper(QuestionAnswerWrapper[] qa) {
        this.qa = qa;
    }

    public QuestionAnswerWrapper[] getQa() {
        return qa;
    }

    public void setQa(QuestionAnswerWrapper[] qa) {
        this.qa = qa;
    }
}
