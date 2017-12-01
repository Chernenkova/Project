package elena.chernenkova.wrappers.recoverpasswordwrappers;

public class EmailWrapper {
    private String email;

    public EmailWrapper(String email) {
        this.email = email;
    }

    public EmailWrapper() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
