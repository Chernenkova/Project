package elena.chernenkova.model.security;

/**
 * Created by 123 on 23.11.2017.
 */
public class UserEnterWrapper {
    private String username;
    private String userPassword;

    public UserEnterWrapper() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
