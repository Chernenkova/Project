package elena.chernenkova.wrappers.cardtask;

public class ChoosingTranslationTaskWrapper {
    private long[] cardsIds;

    public ChoosingTranslationTaskWrapper() {
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
