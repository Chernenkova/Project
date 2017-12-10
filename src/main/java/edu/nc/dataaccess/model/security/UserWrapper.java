package edu.nc.dataaccess.model.security;


public class UserWrapper {
    private String username;
    private String userPassword;
    private String userFirstname;
    private String userLastname;
    private String userDateOfBirth;
    private Integer userRaiting;
    private Boolean enabled;

    public UserWrapper(String username, String userPassword, String userFirstname, String userLastname, String userDateOfBirth, Integer userRaiting, Boolean enabled) {
        this.username = username;
        this.userPassword = userPassword;
        this.userFirstname = userFirstname;
        this.userLastname = userLastname;
        this.userDateOfBirth = userDateOfBirth;
        this.userRaiting = userRaiting;
        this.enabled = enabled;
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

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public void setUserDateOfBirth(String userDateOfBirth) {
        this.userDateOfBirth = userDateOfBirth;
    }

    public Integer getUserRaiting() {
        return userRaiting;
    }

    public void setUserRaiting(Integer userRaiting) {
        this.userRaiting = userRaiting;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

}
