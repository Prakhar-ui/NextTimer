package com.prakhar.nextTimer.Controller;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Service.JwtService;
import com.prakhar.nextTimer.Service.UserService;
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

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/registeruser")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        String result = userService.saveUser(user);
        if ("User Registered Successfully!".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }



    @GetMapping("/getUserByUsername")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = userService.getUser(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping("/getUserById")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.size() == 0) {
            throw new RuntimeException("No User registered");
        }
        return ResponseEntity.ok(users);
    }



    @PostMapping("/editUser")
    public void editUser(@RequestBody EditUserDTO editUserDTO) {
        userService.editUser(editUserDTO);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody UserDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            User user =  userService.getUser(authRequest.getUsername());

            return jwtService.generateToken(user.getId());


        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
