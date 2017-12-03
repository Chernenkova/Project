package edu.nc.dataaccess.wrapper.cardtask;

public class ChoosingTranslationWrapper {
    private ChoosingTranslationCardWrapper[] array;

    public ChoosingTranslationWrapper(ChoosingTranslationCardWrapper[] array) {
        this.array = array;
    }

    public ChoosingTranslationWrapper() {
    }

    public ChoosingTranslationCardWrapper[] getArray() {
        return array;
    }

    public void setArray(ChoosingTranslationCardWrapper[] array) {
        this.array = array;
    }
}
