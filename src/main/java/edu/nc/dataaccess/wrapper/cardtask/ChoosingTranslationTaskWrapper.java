package edu.nc.dataaccess.wrapper.cardtask;

public class ChoosingTranslationTaskWrapper {
    private long[] cardsIds;

    public ChoosingTranslationTaskWrapper() {
        cardsIds = new long[0];
    }

    public ChoosingTranslationTaskWrapper(long[] cardsIds) {
        this.cardsIds = cardsIds;
    }

    public long[] getCardsIds() {
        return cardsIds;
    }

    public void setCardsIds(long[] cardsIds) {
        this.cardsIds = cardsIds;
    }
}
