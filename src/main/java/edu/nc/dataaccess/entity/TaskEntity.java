package edu.nc.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class TaskEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String type;
    @Lob
    private byte[] task;
    @Lob
    private byte[] answer;

    public TaskEntity(String type, byte[] task, byte[] answer) {
        this.type = type;
        this.task = task;
        this.answer = answer;
    }

    public TaskEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getTask() {
        return task;
    }

    public void setTask(byte[] task) {
        this.task = task;
    }

    public byte[] getAnswer() {
        return answer;
    }

    public void setAnswer(byte[] answer) {
        this.answer = answer;
    }
}
