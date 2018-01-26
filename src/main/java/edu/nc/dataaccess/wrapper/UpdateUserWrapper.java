package edu.nc.dataaccess.wrapper;

/**
 * Created by 123 on 26.01.2018.
 */
public class UpdateUserWrapper {
    private String userFirstname;
    private String userLastname;

    public UpdateUserWrapper() {
    }

    public UpdateUserWrapper(String userFirstname, String userLastname) {
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserLastname() {
        return userLastname;
    }

    public void setUserLastname(String userLastname) {
        this.userLastname = userLastname;
    }
}
