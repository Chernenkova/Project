package edu.nc.dataaccess.wrapper.cardtask;

public class ChoosingTranslationCardWrapper {

    private long id;
    private String word;
    private String[] possibleTranslations;

    public ChoosingTranslationCardWrapper() {
    }

    public ChoosingTranslationCardWrapper(long id, String word, String[] possibleTranslations) {
        this.id = id;
        this.word = word;
        this.possibleTranslations = possibleTranslations;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String[] getPossibleTranslations() {
        return possibleTranslations;
    }

    public void setPossibleTranslations(String[] possibleTranslations) {
        this.possibleTranslations = possibleTranslations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
