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

    private String word;

    private String translation;

    public CardEntity(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public CardEntity(CardWrapper wrapper) {
        this.word = wrapper.getWord();
        this.translation = wrapper.getTranslation();
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
