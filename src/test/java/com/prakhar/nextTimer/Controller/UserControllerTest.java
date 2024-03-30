package com.prakhar.nextTimer.Controller;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Service.JwtService;
import com.prakhar.nextTimer.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    @Test
    void saveUserTest() {
        User user = new User();
        user.setEmail("testUser");

        when(userService.saveUser(user)).thenReturn("User Registered Successfully!");

        ResponseEntity<String> responseEntity = userController.saveUser(user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User Registered Successfully!", responseEntity.getBody());
    }

    @Test
    void loginUserTest() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPassword");

        when(userService.loginUser(userDTO)).thenReturn("Login Success!");

        ResponseEntity<String> responseEntity = userController.login(userDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Login Success!", responseEntity.getBody());
    }

    @Test
    void getUserByUsernameTest() {
        String username = "testUser";
        User user = new User();
        user.setEmail(username);

        when(userService.getUser(username)).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.getUserByUsername(username);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        User responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(username, responseBody.getEmail());
    }

    @Test
    void getUserByIdTest() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(user);

        ResponseEntity<User> responseEntity = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userId, responseEntity.getBody().getId());
    }

    @Test
    void getAllUsersTest() {
        User user = new User();
        user.setId(1L);
        List<User> userList = Collections.singletonList(user);

        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());
    }

    @Test
    void editUserTest() {
        EditUserDTO editUserDTO = new EditUserDTO();
        editUserDTO.setEmail("testUser");

        ResponseEntity<String> responseEntity = userController.editUser(editUserDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User edited successfully", responseEntity.getBody());
    }
}
