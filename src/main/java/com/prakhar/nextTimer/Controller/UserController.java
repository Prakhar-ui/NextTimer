package com.prakhar.nextTimer.Controller;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.LoginRequestDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Exception.IncorrectPasswordException;
import com.prakhar.nextTimer.Exception.UserNotFoundException;
import com.prakhar.nextTimer.Service.JwtService;
import com.prakhar.nextTimer.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/registerUser")
    public ResponseEntity<String> saveUser(@RequestBody UserDTO user) throws UserNotFoundException {
        try {
            userService.saveUser(user);
            logger.info("User Registration Success");
            return ResponseEntity.status(HttpStatus.OK).body("User Registration Success");
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            userService.loginUser(loginRequestDTO);
            logger.info("User Login Success!");
            return ResponseEntity.ok("User Login Success!");
        } catch (Exception e) {
            logger.error("Error during user login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        } catch (IncorrectPasswordException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getUserByUsername")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        try {
            User user = userService.getUserByEmail(username);
            logger.info("User found by username: {}", username);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            logger.error("User not found by username: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error during retrieving user by username", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getUserById")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        try {
            User user = userService.getUserById(id);
            logger.info("User found by ID: {}", id);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
            logger.error("User not found by ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error during retrieving user by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            logger.info("Total {} users registered", users.size());
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            logger.error("No User registered");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error during retrieving all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/editUser")
    public ResponseEntity<String> editUser(@RequestBody EditUserDTO editUserDTO) {
        try {
            userService.editUser(editUserDTO);
            logger.info("User edited successfully");
            return ResponseEntity.ok("User edited successfully");
        } catch (Exception e) {
            logger.error("Error during editing user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody LoginRequestDTO authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
            if (authentication.isAuthenticated()) {
                User user = userService.getUserByEmail(authRequest.username());

                logger.info("Authentication successful for user: {}", authRequest.username());
                String token = jwtService.generateToken(user.getId());

                return ResponseEntity.ok(token);
            } else {
                logger.error("Invalid user request for authentication");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }
        } catch (UserNotFoundException e) {
            logger.error("Invalid user request for authentication", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        } catch (Exception e) {
            logger.error("Error during authentication", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
