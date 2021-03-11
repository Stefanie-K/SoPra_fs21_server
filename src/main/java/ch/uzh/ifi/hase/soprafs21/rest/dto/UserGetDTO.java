package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import java.time.LocalDate;

public class UserGetDTO {

    private Long id;
    private String name;
    private String username;
    private UserStatus status;
    private LocalDate dateCreated;
    private String password;
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getStatus() {return status;}

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    //This would return the password to the frontend
    //public String getPassword() {return password;}

    //I think this is needed to set the password (form the frontend?)
    public void setPassword(String password) {this.password = password;}

    public LocalDate getDateCreated() {return dateCreated;}

    public void setDateCreated(LocalDate dateCreated) {this.dateCreated = dateCreated;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}
}
