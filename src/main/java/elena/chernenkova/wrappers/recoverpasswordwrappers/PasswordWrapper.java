package elena.chernenkova.wrappers.recoverpasswordwrappers;

public class PasswordWrapper {
    private String password;

    public PasswordWrapper(String password) {
        this.password = password;
    }

    public PasswordWrapper() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
