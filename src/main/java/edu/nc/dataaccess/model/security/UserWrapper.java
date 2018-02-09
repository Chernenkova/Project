package edu.nc.dataaccess.model.security;


public class UserWrapper {
    private String username;
    private String userPassword;
    private String userFirstname;
    private String userLastname;
    private Integer userRaiting;

    public UserWrapper(String username, String userPassword, String userFirstname, String userLastname, Integer userRaiting) {
        this.username = username;
        this.userPassword = userPassword;
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
        this.userRaiting = userRaiting;
    }

    public UserWrapper(String username, String userPassword) {
        this.username = username;
        this.userPassword = userPassword;
        this.userFirstname = null;
        this.userLastname = null;
        this.userRaiting = null;
    }

    public UserWrapper() {
    }

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

    public Integer getUserRaiting() {
        return userRaiting;
    }

    public void setUserRaiting(Integer userRaiting) {
        this.userRaiting = userRaiting;
    }

}
