package edu.nc.dataaccess.entity;

import javax.persistence.*;

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
    @OneToOne
    private User author;

    private byte[] nameBytes;
    private Integer reward;
    private Integer minCost;


    public TaskEntity(String type, byte[] task, byte[] answer, User author, String name, Integer reward, Integer minCost) {
        this.type = type;
        this.task = task;
        this.answer = answer;
        this.author = author;
        this.nameBytes = name.getBytes();
        this.reward = reward;
        this.minCost = minCost;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getName() {
        return new String(nameBytes);
    }

    public void setName(String name) {
        this.nameBytes = name.getBytes();
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public Integer getMinCost() {
        return minCost;
    }

    public void setMinCost(Integer minCost) {
        this.minCost = minCost;
    }

    public byte[] getNameBytes() {
        return nameBytes;
    }

    public void setNameBytes(byte[] nameBytes) {
        this.nameBytes = nameBytes;
    }
}
