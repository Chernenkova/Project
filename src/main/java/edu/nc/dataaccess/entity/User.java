package edu.nc.dataaccess.entity;

import edu.nc.dataaccess.model.security.Authority;
import edu.nc.dataaccess.model.security.AuthorityName;
import edu.nc.dataaccess.model.security.UserEnterWrapper;
import edu.nc.dataaccess.model.security.UserWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(name = "LOGIN", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Column(name = "PASSWORD", length = 100)
    @NotNull
    @Size(min = 4, max = 100)
    private String userPassword;

    @Column(name = "FIRSTNAME", length = 50)
    @Size(max = 50)
    private byte[] firstnameBytes;

    @Column(name = "LASTNAME", length = 50)
    @Size(max = 50)
    private byte[] lastnameBytes;

    @Column(name = "RAITING", length = 50)
    private Integer raiting;

    @Column(name = "ENABLED")
    private Boolean enabled;

    @Column(name = "LASTPASSWORDRESETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;


    @OneToMany
    private List<TaskProgressEntity> tasks = new ArrayList<>();


    public User(UserWrapper userWrapper, Authority authority) {
        this.username = userWrapper.getUsername();
        this.userPassword = userWrapper.getUserPassword();
        this.firstnameBytes = userWrapper.getUserFirstname().getBytes();
        this.lastnameBytes = userWrapper.getUserLastname().getBytes();
        this.raiting = 0;
        this.enabled = Boolean.TRUE;
        this.lastPasswordResetDate = new Date();
        authorities = new ArrayList<>();
        authorities.add(authority);
    }

    public User() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getFirstname() {
        return new String(firstnameBytes);
    }

    public void setFirstname(String firstname) {
        this.firstnameBytes = firstname.getBytes();
    }

    public String getLastname() {
        return new String(lastnameBytes);
    }

    public void setLastname(String lastname) {
        this.lastnameBytes = lastname.getBytes();
    }

    public Integer getRaiting() {
        return raiting;
    }

    public void setRaiting(Integer raiting) {
        this.raiting = raiting;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Date lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public List<TaskProgressEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskProgressEntity> tasks) {
        this.tasks = tasks;
    }

    public byte[] getFirstnameBytes() {
        return firstnameBytes;
    }

    public void setFirstnameBytes(byte[] firstnameBytes) {
        this.firstnameBytes = firstnameBytes;
    }

    public byte[] getLastnameBytes() {
        return lastnameBytes;
    }

    public void setLastnameBytes(byte[] lastnameBytes) {
        this.lastnameBytes = lastnameBytes;
    }


}
