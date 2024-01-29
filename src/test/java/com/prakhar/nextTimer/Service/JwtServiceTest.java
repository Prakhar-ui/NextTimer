package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.Entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    @DisplayName("Test Generate Token")
    public void testGenerateToken() {

    }


}
