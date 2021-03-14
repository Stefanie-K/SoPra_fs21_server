package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LogedinUserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test //Test GET: /users .ok
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        // given
        User user = new User();
        user.setName("Firstname Lastname");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);

        List<User> allUsers = Collections.singletonList(user);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
    }

    @Test //Test GET: /users .failed
    public void createUser_invalidInput_userNotCreated() throws Exception {
        // given
        User user = new User();
        user.setUserID(1L);
        //user.setName("Test User");
        user.setUsername("usernameTest");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        //userPostDTO.setName("Test User");
        userPostDTO.setUsername("usernameTest");

        given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("User is already in DB.")));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    @Test//Test GET: /users/userID .ok
    public void getUser_with_UserID() throws Exception {
        // given
        User user = new User();
        user.setName("Lastname");
        user.setUsername("testname");
        user.setStatus(UserStatus.OFFLINE);
        long userID = 1;
        user.setUserID(userID);

        // this mocks the UserService -> we define above what the userService should return when getUsers() is called
        given(userService.getUserById(userID)).willReturn(user);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/1").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    @Test //Test GET: /users/userID .failed //works but not really
    public void getUser_with_UserID_invaildID() throws Exception {
        // given
        User user = new User();
        user.setUserID(1L);
        //user.setName("Test User");
        user.setUsername("usernameTest");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        //userPostDTO.setName("Test User");
        userPostDTO.setUsername("usernameTest");

        given(userService.getUserById(1L)).willThrow(new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, String.format("No such user.")));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isMethodNotAllowed());
    }

    @Test //Test POST: /users
    public void createUser_validInput_userCreated() throws Exception {
        // given
        User user = new User();
        user.setUserID(1L);
        user.setName("Test User");
        user.setUsername("testUsername");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("Test User");
        userPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user.getUserID().intValue())))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
    }

    @Test //Test POST: /users/login
    public void loginUser() throws Exception {
        // given
        User user = new User();
        user.setUserID(1L);
        user.setName("Test User");
        user.setUsername("testUsername");
        user.setPassword("123");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        LogedinUserPostDTO logedinUserPostDTO = new LogedinUserPostDTO();
        logedinUserPostDTO.setPassword("123");
        logedinUserPostDTO.setUsername("testUsername");

        given(userService.createUser(Mockito.any())).willReturn(user);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(logedinUserPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test //Test PUT: /users/userID .ok
    public void updateUser() throws Exception {

        User user = new User();
        user.setUserID(1L);
        user.setName("Test User");
        user.setUsername("testUsername");
        user.setPassword("123");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        LogedinUserPostDTO logedinUserPostDTO = new LogedinUserPostDTO();
        logedinUserPostDTO.setUsername("ChangedUsername");
        logedinUserPostDTO.setUserID(1L);

        given(userService.getUserById(1L)).willReturn(user);

        mockMvc.perform( MockMvcRequestBuilders
                .put("/users/1")
                .content(asJsonString(logedinUserPostDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
                //.andExpect(jsonPath("$.username", is(user.getUsername()))); //this does not work yet
    }

    @Test //Test PUT: /users/userID .failed
    public void updateUser_failed() throws Exception {
        User user = new User();
        user.setUserID(1L);
        user.setName("Test User");
        user.setUsername("testUsername");
        user.setPassword("123");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        LogedinUserPostDTO logedinUserPostDTO = new LogedinUserPostDTO();
        logedinUserPostDTO.setUsername("ChangedUsername");
        logedinUserPostDTO.setUserID(5L);

        given(userService.getUserById(1L)).willReturn(user);

        mockMvc.perform( MockMvcRequestBuilders
                .put("/users/5")
                .content(asJsonString(logedinUserPostDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

        /**
         * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
         * Input will look like this: {"name": "Test User", "username": "testUsername"}
         * @param object
         * @return string
         */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}