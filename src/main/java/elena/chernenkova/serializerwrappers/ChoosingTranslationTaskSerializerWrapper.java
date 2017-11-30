package elena.chernenkova.serializerwrappers;

import java.io.Serializable;

public class ChoosingTranslationTaskSerializerWrapper implements Serializable{
    private long[] cardsIds;

    public ChoosingTranslationTaskSerializerWrapper() {
    }

    public ChoosingTranslationTaskSerializerWrapper(long[] cardsIds) {
        this.cardsIds = cardsIds;
    }

    public long[] getCardsIds() {
        return cardsIds;
    }

    public void setCardsIds(long[] cardsIds) {
        this.cardsIds = cardsIds;
    }
}
