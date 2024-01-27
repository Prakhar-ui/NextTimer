package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
public class UserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Test saveUser method with a new user")
    public void testSaveUser_NewUser() {
        // Mocking behavior for findByEmail when the email is not present
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Capture the argument passed to passwordEncoder.encode
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        when(passwordEncoder.encode(passwordCaptor.capture())).thenReturn("encodedPassword");

        // Capture the argument passed to userRepository.save
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // Call the method
        String result = userService.saveUser(new User("NewUser",25,"newuser@example.com","password"));

        // Verify that userRepository.save was called once
        verify(userRepository, times(1)).save(userCaptor.capture());


        // Assert the properties of the saved user
        assertEquals("password", passwordCaptor.getValue());

        // Retrieve the saved user from the captured argument
        User savedUser = userCaptor.getValue();

        assertEquals("NewUser", savedUser.getName());
        assertEquals(25, savedUser.getAge());
        assertEquals("newuser@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());

        System.out.println("result" + result);
        // Assert the result
        assertEquals("User Registered Successfully!", result);
    }


    @Test
    @DisplayName("Test saveUser method with an existing user")
    public void testSaveUser_ExistingUser() {
        // Mocking behavior for findByEmail when the email is already present
        User existingUser = new User("ExistingUser", 30, "existinguser@example.com", "existingPassword");
        when(userRepository.findByEmail("existinguser@example.com")).thenReturn(Optional.of(existingUser));

        // Call the method
        String result = userService.saveUser(new User("ExistingUser", 30, "existinguser@example.com", "password"));

        // Verify that userRepository.findByEmail was called with the correct email
        verify(userRepository, atLeastOnce()).findByEmail("existinguser@example.com");

        // Verify that userRepository.save was not called
        verify(userRepository, never()).save(any(User.class));

        // Assert the result
        assertEquals("Username already present", result);
    }

    @Test
    @DisplayName("Test Login User with Password Matching")
    public void testLoginUser_PasswordMatched() {

        // Mocking behavior for findByEmail when the email is already present
        User existingUser = new User("User", 30, "user@example.com", "encodedPassword");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existingUser));

        // Mocking behavior for passwordEncoder
        when(passwordEncoder.matches(eq("rawPassword"), eq("encodedPassword"))).thenReturn(true);

        // Call the method
        String result = userService.loginUser(new UserDTO( "user@example.com", "rawPassword"));

        // Assert the result
        assertEquals("Login Success", result);
    }

    @Test
    @DisplayName("Test Login User with Password Not Matching")
    public void testLoginUser_PasswordNotMatched() {
        // Mocking behavior for findByEmail when the email is already present
        User existingUser = new User("User", 30, "user@example.com", "encodedPassword");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existingUser));

        // Mocking behavior for passwordEncoder
        when(passwordEncoder.matches(eq("rawPassword"), eq("encodedPassword"))).thenReturn(true);

        // Call the method
        String result = userService.loginUser(new UserDTO( "user@example.com", "rawPassword2"));

        // Assert the result
        assertEquals("Password not matching", result);
    }

    @Test
    @DisplayName("Test Login User with Email Not Present")
    public void testLoginUser_EmailNotPresent() {
// Mocking behavior for findByEmail when the email is already present
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        // Call the method
        String result = userService.loginUser(new UserDTO( "user@example.com", "rawPassword"));

        // Assert the result
        assertEquals("Username not present", result);
    }


}

