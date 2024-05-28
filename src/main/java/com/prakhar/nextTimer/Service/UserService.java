package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.LoginRequestDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Exception.IncorrectPasswordException;
import com.prakhar.nextTimer.Exception.UserNotFoundException;

import java.util.List;

public interface UserService {
    // CRUD operations
    void saveUser(UserDTO userDTO) throws UserNotFoundException;

    void editUser(EditUserDTO editUserDTO) throws UserNotFoundException;

    User getUserById(Long id) throws UserNotFoundException;

    User getUserByEmail(String username) throws Exception, UserNotFoundException;

    List<User> getAllUsers();

    // Authentication
    void loginUser(LoginRequestDTO loginRequestDTO) throws IncorrectPasswordException;

}