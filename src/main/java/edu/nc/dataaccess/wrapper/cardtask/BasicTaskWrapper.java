package edu.nc.dataaccess.wrapper.cardtask;

public class BasicTaskWrapper {
    private CardWrapper[] array;

    public BasicTaskWrapper(CardWrapper[] array) {
        this.array = array;
    }

    public BasicTaskWrapper() {
    }

    public CardWrapper[] getArray() {
        return array;
    }

    public void setArray(CardWrapper[] array) {
        this.array = array;
    }
}
