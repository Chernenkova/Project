package elena.chernenkova.wrappers.login;

/**
 * Created by 123 on 01.12.2017.
 */
public class TokenWrapper {
    private String token;

    public TokenWrapper() {}

    public TokenWrapper(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
