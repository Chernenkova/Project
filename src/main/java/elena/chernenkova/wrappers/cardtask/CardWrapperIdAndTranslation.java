package elena.chernenkova.wrappers.cardtask;

public class CardWrapperIdAndTranslation {
    private Long id;
    private String translation;

    public CardWrapperIdAndTranslation(Long id, String translation) {
        this.id = id;
        this.translation = translation;
    }

    public CardWrapperIdAndTranslation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
