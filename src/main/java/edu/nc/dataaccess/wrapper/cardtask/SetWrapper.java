package edu.nc.dataaccess.wrapper.cardtask;

public class SetWrapper {

    private String name;
    private Integer reward;
    private Integer minrate;
    /**
     * {@link edu.nc.common.GeneralSettings#WRITING_TYPE}
     * {@link edu.nc.common.GeneralSettings#CHOOSING_TASK_BASIC_TYPE}
     */
    private String type;
    private CardWrapper[] array;

    public SetWrapper() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public Integer getMinrate() {
        return minrate;
    }

    public void setMinrate(Integer minrate) {
        this.minrate = minrate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CardWrapper[] getArray() {
        return array;
    }

    public void setArray(CardWrapper[] array) {
        this.array = array;
    }
}
