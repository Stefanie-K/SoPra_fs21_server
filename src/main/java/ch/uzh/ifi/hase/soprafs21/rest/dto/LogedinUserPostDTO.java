package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class LogedinUserPostDTO {

    private String username;

    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //I think this is needed to get the password, backend gets it from frontend
    public String getPassword() {return password;}

    //I don't think this is needed
    //public void setPassword(String password) {this.password = password;}
}
