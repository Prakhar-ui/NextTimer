package com.prakhar.nextTimer.Service.Impl;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.LoginRequestDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Exception.IncorrectPasswordException;
import com.prakhar.nextTimer.Exception.UserNotFoundException;
import com.prakhar.nextTimer.Repository.UserRepository;
import com.prakhar.nextTimer.Service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(UserDTO userDTO) throws UserNotFoundException {
        try {
            if (userRepository.findByEmail(userDTO.email()).isPresent()) {
                logger.info("Username already present: {}", userDTO.email());
            } else {
                User user = new User();
                user.setName(userDTO.name());
                user.setAge(userDTO.age());
                user.setEmail(userDTO.email());
                user.setPassword(passwordEncoder.encode(userDTO.password()));
                userRepository.save(user);
                logger.info("User Registered Successfully: {}", user.getEmail());
            }
        } catch (Exception e) {
            String errorMessage = "Error saving user";
            logger.error("Error saving user", e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public void editUser(EditUserDTO editUserDTO) throws UserNotFoundException {
        try {
            User user = getUserById(editUserDTO.id());
            if (StringUtils.isNotBlank(editUserDTO.password())) {
                user.setPassword(passwordEncoder.encode(editUserDTO.password()));
            }
            user.setName(editUserDTO.name());
            user.setAge(editUserDTO.age());
            user.setEmail(editUserDTO.email());
            userRepository.save(user);
            logger.info("User edited successfully: {}", user.getEmail());
        } catch (Exception e) {
            String errorMessage = "Error editing user";
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public User getUserById(Long id) throws UserNotFoundException {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            logger.info("User Retrieved successfully with id: {}", id);
            return user;
        } catch (Exception e) {
            String errorMessage = "Error getting user by id";
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public User getUserByEmail(String username) throws UserNotFoundException {
        try {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
            return user;
        } catch (Exception e) {
            String errorMessage = "Error getting user by email";
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            logger.info("All Users Fetched");
            return users;

        } catch (Exception e) {
            String errorMessage = "Error getting all users";
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public void loginUser(LoginRequestDTO loginRequestDTO) throws IncorrectPasswordException {
        User user = null;
        try {
            user = getUserByEmail(loginRequestDTO.username());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            logger.info("Login Success for user: {}", loginRequestDTO.username());
        } else {
            throw new IncorrectPasswordException("Incorrect password for user: " + loginRequestDTO.username());
        }
    }
}