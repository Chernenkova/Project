package edu.nc.dataaccess.wrapper.taskprogress;

public class TaskInfoWrapper {

    private String name;
    private String type;
    private int reward;
    private long id;
    private boolean completed;

    public TaskInfoWrapper() {
    }

    public TaskInfoWrapper(String name, String type, int reward, long id, boolean completed) {
        this.name = name;
        this.type = type;
        this.reward = reward;
        this.id = id;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
