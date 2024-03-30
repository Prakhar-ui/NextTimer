package com.prakhar.nextTimer.Controller;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Service.JwtService;
import com.prakhar.nextTimer.Service.UserService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        try {
            String result = userService.saveUser(user);
            logger.info("User Registration: {}", result);
            return ResponseEntity.status(result.equals("User Registered Successfully!") ? HttpStatus.OK : HttpStatus.UNAUTHORIZED).body(result);
        } catch (Exception e) {
            logger.error("Error during user registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO) {
        try {
            String result = userService.loginUser(userDTO);
            logger.info("User Login Success: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error during user login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/getUserByUsername")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        try {
            User user = userService.getUser(username);
            System.out.println(user);
            logger.info("User found by username: {}", username);
            return ResponseEntity.ok(user);
        } catch (UsernameNotFoundException e) {
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
        }
    }

    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody UserDTO authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            System.out.println(authentication.getPrincipal());
            System.out.println(authentication.isAuthenticated());
            System.out.println(authentication.getCredentials());
            if (authentication.isAuthenticated()) {
                User user = userService.getUser(authRequest.getUsername());
                System.out.println(user);

                logger.info("Authentication successful for user: {}", authRequest.getUsername());
                String token = jwtService.generateToken(user.getId());
                System.out.println(token);

                return ResponseEntity.ok(token);
            } else {
                logger.error("Invalid user request for authentication");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
            }
        } catch (UsernameNotFoundException e) {
            logger.error("Invalid user request for authentication", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        } catch (Exception e) {
            logger.error("Error during authentication", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
