package edu.nc.dataaccess.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
public class RegUserEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String login;
    private String passwordHash;
    private Date date;
    private String uuid;

    public RegUserEntity(String login, String password) {
        this.login = login;
        this.passwordHash = password;
        this.date = new Date();
        this.uuid = "reg01WK-" + UUID.randomUUID().toString();
    }

    public RegUserEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
