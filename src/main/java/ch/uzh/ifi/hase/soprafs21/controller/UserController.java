package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LogedinUserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
//...
/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @PostMapping("/users/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        System.out.println("CreateUser : register");
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        System.out.println("convert user"+ userInput.getName()+ "to PostDTO");

        // create user
        User createdUser = userService.createUser(userInput);
        //log in
        userService.login(createdUser);

        System.out.println("created"+ userInput.getName());
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO loginUser(@RequestBody LogedinUserPostDTO logedinUserPostDTO) {
        // convert API user to internal representation
        System.out.println("message");
        User userInput = DTOMapper.INSTANCE.convertLogedinUserPostDTOtoEntity(logedinUserPostDTO);

        // check if login is possible
        User logedInUser = userService.checkIfLoginPossible(userInput);
        //if login was possible set status to online
        userService.login(logedInUser);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(logedInUser);
    }

    @GetMapping("/users/{userID}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDTO getUserByUserID(@PathVariable("userID") Long userID) {
        // fetch all users in the internal representation
        User user = userService.getUserById(userID);
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        return userGetDTO;
    }

    @PutMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO updateUser(@RequestBody LogedinUserPostDTO logedinUserPostDTO) {
        // convert API user to internal representation
        System.out.println("Username: "+logedinUserPostDTO.getUsername());
        User user = userService.getUserById(logedinUserPostDTO.getUserID());
        User userUpdated = userService.updateUser(user, logedinUserPostDTO);
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(userUpdated);

        return userGetDTO;
    }

    @PutMapping("/user/{token}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void logout(@PathVariable("token") String token) {
        // convert API user to internal representation
        User user = userService.getUserByToken(token);
        UserService.logout(user);
    }
}
