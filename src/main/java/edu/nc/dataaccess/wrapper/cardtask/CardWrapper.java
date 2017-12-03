package edu.nc.dataaccess.wrapper.cardtask;

public class CardWrapper {
    private String word;
    private String translation;

    public CardWrapper(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public CardWrapper() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
