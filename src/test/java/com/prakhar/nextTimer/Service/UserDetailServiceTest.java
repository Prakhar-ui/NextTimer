package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @Test
    @DisplayName("Test Load User By Username")
    public void testLoadUserByUsername() {
        User existingUser = new User(1L, "ExistingUser", 30, "existinguser@example.com", "existingPassword");

        // Capture the argument passed to userRepository.findByEmail
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);

        // Mocking behavior for findByEmail when the email is not present
        when(userRepository.findByEmail(emailCaptor.capture()))
                .thenReturn(Optional.of(existingUser));

        UserDetails result = userDetailService.loadUserByUsername("ExistingUser");

        // Verify that userRepository.findByEmail was called once with the correct argument
        verify(userRepository, times(1)).findByEmail("ExistingUser");

        assertEquals("ExistingUser", emailCaptor.getValue());
        assertEquals("1", result.getUsername());
        assertEquals("existingPassword", result.getPassword());

        // Assert the authorities - Convert to a Set for comparison
        Set<String> expectedAuthorities = Collections.singleton("ROLE_USER");
        Set<String> actualAuthorities = result.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        assertEquals(expectedAuthorities, actualAuthorities);
    }
}
