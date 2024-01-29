package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.DTO.EditUserDTO;
import com.prakhar.nextTimer.DTO.UserDTO;
import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {


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

    @Test
    @DisplayName("Test Get All Users")
    public void testGetAllUsers() {

        List<User> userList = Arrays.asList(
                new User("User1", 23, "User1@gmail.com", "encodedPassword1"),
                new User("User2", 24, "User2@gmail.com", "encodedPassword2"),
                new User("User3", 25, "User3@gmail.com", "encodedPassword3"),
                new User("User4", 26, "User4@gmail.com", "encodedPassword4")
        );

        when(userRepository.findAll()).thenReturn(userList);

        // Call the method
        List<User> result = userService.getAllUsers();

        // Assert the result
        assertEquals(userList, result);
    }

    @Test
    @DisplayName("Test Get User By Username")
    public void testGetUserByUsername() {

        User userByUsername = new User("User1", 23, "User1@gmail.com", "encodedPassword1");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(userByUsername));

        // Call the method
        User result = userService.getUser("User1@gmail.com");

        // Assert the result
        assertEquals(userByUsername, result);
    }

    @Test
    @DisplayName("Test Get Task By Id")
    public void testGetUserById() {

        User userById = new User(1L,"User1", 23, "User1@gmail.com", "encodedPassword1");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userById));

        // Call the method
        User result = userService.getUserById(1L);

        // Assert the result
        assertEquals(userById, result);
    }

    @Test
    @DisplayName("Test Edit User with Not Null Password")
    public void testEditUser_withPassword() {

        User userById = new User("User1", 23, "User1@gmail.com", "encodedPassword1");

        // Capture the argument passed to passwordEncoder.encode
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);

        when(userRepository.findById(idCaptor.capture())).thenReturn(Optional.of(userById));
        when(userRepository.save(userCaptor.capture())).thenReturn(new User("NewUser",25,"newuser@example.com","encodedPassword"));
        when(passwordEncoder.encode(passwordCaptor.capture())).thenReturn("encodedPassword");

        // Call the method
        userService.editUser(new EditUserDTO("1","NewUser",25,"newuser@example.com","password"));

        // Assert the properties of the saved user
        assertEquals(1L, idCaptor.getValue());
        assertEquals("password", passwordCaptor.getValue());

        // Retrieve the saved user from the captured argument
        User savedUser = userCaptor.getValue();

        assertEquals("NewUser", savedUser.getName());
        assertEquals(25, savedUser.getAge());
        assertEquals("newuser@example.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());

    }


    @Test
    @DisplayName("Test Edit User with Null Password")
    public void testEditUser_withoutPassword() {
        User userById = new User("User1", 23, "User1@gmail.com", "encodedPassword1");

        // Capture the argument passed to passwordEncoder.encode
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.findById(idCaptor.capture())).thenReturn(Optional.of(userById));
        when(userRepository.save(userCaptor.capture())).thenReturn(new User("NewUser",25,"newuser@example.com",""));

        // Call the method
        userService.editUser(new EditUserDTO("1","NewUser",25,"newuser@example.com",""));

        // Assert the properties of the saved user
        assertEquals(1L, idCaptor.getValue());

        // Retrieve the saved user from the captured argument
        User savedUser = userCaptor.getValue();

        assertEquals("NewUser", savedUser.getName());
        assertEquals(25, savedUser.getAge());
        assertEquals("newuser@example.com", savedUser.getEmail());
        assertEquals("encodedPassword1", savedUser.getPassword());
    }

    @Test
    @DisplayName("Test Edit User with User Not Found")
    public void testEditUser_withoutUser() {
// Set up test data
        EditUserDTO editUserDTO = new EditUserDTO("1", "NewUser", 25, "newuser@example.com", "password");

        // Mock behavior for userRepository.findById() to return an empty Optional
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call the method and assert an exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.editUser(editUserDTO));

        // Verify that the error message contains the expected text
        assertTrue(exception.getMessage().contains("User not Found!"));

        // Verify that userRepository.save was not called (since user was not found)
        verify(userRepository, never()).save(any(User.class));
    }


}

