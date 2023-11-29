package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UserRepository userRepository;

    public String saveUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            return "Username already present";
        }
        else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "Username saved. " + " username: " + user.getUsername() + ", password: " + user.getPassword();
        }
    }

    public String loginUser(UserDTO user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getUsername());
        if (optionalUser.isPresent()){
            if (passwordEncoder.matches(user.getPassword(),optionalUser.get().getPassword())){
                return "Login Success";
            }
            else {
                return "Password not matching";
            }
        }
        return "username not present";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
