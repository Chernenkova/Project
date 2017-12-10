package edu.nc.dataaccess.entity;


import javax.persistence.*;

@Entity
public class TaskProgressEntity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private TaskEntity task;
    private Integer totalScore;
    private Integer score;

    @Enumerated(EnumType.STRING)
    private TaskProgressStatus status;

    public TaskProgressEntity() {
    }

    public TaskProgressEntity(TaskEntity task, Integer totalScore, Integer score, TaskProgressStatus status) {
        this.task = task;
        this.totalScore = totalScore;
        this.score = score;
        this.status = status;
    }

    public TaskProgressEntity(TaskEntity task) {
        this.task = task;
        this.totalScore = 0;
        this.score = 0;
        this.status = TaskProgressStatus.IN_PROGRESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskEntity getTask() {
        return task;
    }

    public void setTask(TaskEntity task) {
        this.task = task;
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

    public TaskProgressStatus getStatus() {
        return status;
    }

    public void setStatus(TaskProgressStatus status) {
        this.status = status;
    }
}
