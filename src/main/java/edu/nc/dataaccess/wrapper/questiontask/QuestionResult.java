package edu.nc.dataaccess.wrapper.questiontask;

public class QuestionResult {
    private Integer totalScore;
    private Integer score;
    private QuestionTaskAnswerWrapper answers;
    private Integer rating;

    public QuestionResult() {
    }

    public QuestionResult(Integer totalScore, Integer score, QuestionTaskAnswerWrapper answers, Integer rating) {
        this.totalScore = totalScore;
        this.score = score;
        this.answers = answers;
        this.rating = rating;
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
