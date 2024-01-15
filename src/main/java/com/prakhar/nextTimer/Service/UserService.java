package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String saveUser(User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                logger.info("Username already present: {}", user.getEmail());
                return "Username already present";
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                logger.info("User Registered Successfully: {}", user.getEmail());
                return "User Registered Successfully!";
            }
        } catch (Exception e) {
            logger.error("Error saving user", e);
            throw e;
        }
    }

    public String loginUser(UserDTO user) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(user.getUsername());
            if (optionalUser.isPresent()) {
                User foundUser = optionalUser.get();
                if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                    logger.info("Login Success for user: {}", user.getUsername());
                    return "Login Success";
                } else {
                    logger.info("Password not matching for user: {}", user.getUsername());
                    return "Password not matching";
                }
            }
            logger.info("Username not present: {}", user.getUsername());
            return "Username not present";
        } catch (Exception e) {
            logger.error("Error logging in user", e);
            throw e;
        }
    }

    public List<User> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            logger.info("All Users Fetched");
            return users;
        } catch (Exception e) {
            logger.error("Error getting all users", e);
            throw e;
        }
    }

    public User getUser(String username) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(username);
            if (optionalUser.isPresent()) {
                logger.info("User Retrieved successfully with username: {}", username);
            } else {
                logger.info("User Not Present with username: {}", username);
            }
            return optionalUser.orElse(null);
        } catch (Exception e) {
            logger.error("Error getting user by username: {}", username, e);
            throw e;
        }
    }

    public void editUser(EditUserDTO editUserDTO) {
        try {
            Optional<User> optionalUser = userRepository.findById(Long.valueOf(editUserDTO.getId()));
            if (optionalUser.isPresent()) {
                User userToUpdate = optionalUser.get();
                if (StringUtils.isNotBlank(editUserDTO.getPassword())) {
                    userToUpdate.setPassword(passwordEncoder.encode(editUserDTO.getPassword()));
                }
                userToUpdate.setName(editUserDTO.getName());
                userToUpdate.setAge(editUserDTO.getAge());
                userToUpdate.setEmail(editUserDTO.getEmail());
                userRepository.save(userToUpdate);
                logger.info("User edited successfully: {}", userToUpdate.getEmail());
            } else {
                logger.error("Error finding user: {}", Long.valueOf(editUserDTO.getId()));
                throw new RuntimeException("User not Found!");
            }
        } catch (Exception e) {
            logger.error("Error editing user", e);
            throw e;
        }
    }

    public User getUserById(Long id) {
        try {
            Optional<User> optionalUser = userRepository.findById(id);
            if (optionalUser.isPresent()) {
                logger.info("User Retrieved successfully with id: {}", id);
            } else {
                logger.info("User Not Present with id: {}", id);
            }

            return optionalUser.orElse(null);
        } catch (Exception e) {
            logger.error("Error getting user by ID: {}", id, e);
            throw e;
        }
    }
}