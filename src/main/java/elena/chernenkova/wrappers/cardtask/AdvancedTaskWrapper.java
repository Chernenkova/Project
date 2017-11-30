package elena.chernenkova.wrappers.cardtask;

public class AdvancedTaskWrapper {
    private ChoosingTranslationCardWrapper[] array;

    public AdvancedTaskWrapper(ChoosingTranslationCardWrapper[] array) {
        this.array = array;
    }

    public AdvancedTaskWrapper() {
    }

    public ChoosingTranslationCardWrapper[] getArray() {
        return array;
    }

    public void setArray(ChoosingTranslationCardWrapper[] array) {
        this.array = array;
    }
}
