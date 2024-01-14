package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.DTO.TimerDTO;
import com.prakhar.nextTimer.Entity.Task;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.TaskRepository;
import com.prakhar.nextTimer.Repository.UserRepository;
import io.micrometer.common.util.StringUtils;
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
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Username already present";
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "User Registered Successfully!";
        }
    }

    public String loginUser(UserDTO user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getUsername());
        if (optionalUser.isPresent()) {
            if (passwordEncoder.matches(user.getPassword(), optionalUser.get().getPassword())) {
                return "Login Success";
            } else {
                return "Password not matching";
            }
        }
        return "username not present";
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(String username) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        return optionalUser.orElse(null);
    }

    public void editUser(EditUserDTO editUserDTO) {
        Optional<User> optionalUser = userRepository.findById(Long.valueOf(editUserDTO.getId()));
        if (optionalUser.isPresent()) {
            User new_user = optionalUser.get();
            if (StringUtils.isNotBlank(editUserDTO.getPassword())) { // Check for null or empty string
                new_user.setPassword(passwordEncoder.encode(editUserDTO.getPassword()));
            }
            new_user.setName(editUserDTO.getName());
            new_user.setAge(editUserDTO.getAge());
            new_user.setEmail(editUserDTO.getEmail());
            userRepository.save(new_user);
        } else {
            throw new RuntimeException("User not Found!");
        }
    }

    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }


}
