package edu.nc.dataaccess.wrapper.questiontask;

public class QuestionResult {
    private Integer totalScore;
    private Integer score;
    private QuestionTaskAnswerWrapper answers;

    public QuestionResult() {
    }

    public QuestionResult(Integer totalScore, Integer score, QuestionTaskAnswerWrapper answers) {
        this.totalScore = totalScore;
        this.score = score;
        this.answers = answers;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public QuestionTaskAnswerWrapper getAnswers() {
        return answers;
    }

    public void setAnswers(QuestionTaskAnswerWrapper answers) {
        this.answers = answers;
    }
}
