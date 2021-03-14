package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class LogedinUserPostDTO {

    private String username;

    private String password;

    private String birthdate;

    private Long userID;

    public String getUsername() {return username;}

    public void setUsername(String username) {
        this.username = username;
    }

    //I think this is needed to get the password, backend gets it from frontend
    public String getPassword() {return password;}

    //needed for the test
    public void setPassword(String password) {this.password = password;}

    public String getBirthdate() {return birthdate;}

    public void setBirthdate(String birthdate) {this.birthdate = birthdate;}

    public Long getUserID() {return userID;}

    public void setUserID(Long userID) {this.userID = userID;}


}
