package com.prakhar.nextTimer.Contorller;

import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Service.JwtService;
import com.prakhar.nextTimer.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<String> saveUser(@RequestBody User user){
        String result = userService.saveUser(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO user){
        String result = userService.loginUser(user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(@RequestParam String username){
        User user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }


    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody UserDTO authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }
}
