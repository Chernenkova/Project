package edu.nc.dataaccess.entity;

import edu.nc.dataaccess.wrapper.cardtask.CardWrapper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CardEntity {
    @Id
    @GeneratedValue
    private Long id;

    private byte[] wordBytes;

    private byte[] translationBytes;

    public CardEntity(CardWrapper wrapper) {
        this.wordBytes = wrapper.getWord().getBytes();
        this.translationBytes = wrapper.getTranslation().getBytes();
    }

    public CardEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return new String(wordBytes);
    }

    public void setWord(String word) {
        this.wordBytes = word.getBytes();
    }

    public String getTranslation() {
        return new String(translationBytes);
    }

    public void setTranslation(String translation) {
        this.translationBytes = translation.getBytes();
    }

    public byte[] getWordBytes() {
        return wordBytes;
    }

    public void setWordBytes(byte[] wordBytes) {
        this.wordBytes = wordBytes;
    }

    public byte[] getTranslationBytes() {
        return translationBytes;
    }

    public void setTranslationBytes(byte[] translationBytes) {
        this.translationBytes = translationBytes;
    }
}
