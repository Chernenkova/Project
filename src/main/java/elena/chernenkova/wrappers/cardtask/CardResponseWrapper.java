package elena.chernenkova.wrappers.cardtask;

public class CardResponseWrapper {
    private String translation;

    public CardResponseWrapper(String translation) {
        this.translation = translation;
    }

    public CardResponseWrapper() {
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
