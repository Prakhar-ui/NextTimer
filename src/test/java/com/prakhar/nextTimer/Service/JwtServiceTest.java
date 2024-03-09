package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.Entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.jsonwebtoken.Claims;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    private final PasswordEncoder passwordEncoder;

    public JwtServiceTest(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Test
    @DisplayName("Test Generate Token")
    public void testGenerateToken() throws NoSuchFieldException, IllegalAccessException {
        // Given
        Long id = 1L;

        // Manually set the SECRET field using reflection
        Field secretField = jwtService.getClass().getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        // When
        String token = jwtService.generateToken(id);

        // Then
        assertNotNull(token);
    }

    @Test
    @DisplayName("Get SignIn Key")
    public void testGetSignkey() throws NoSuchFieldException, IllegalAccessException {
        // Given
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        // Manually set the SECRET field using reflection
        Field secretField = jwtService.getClass().getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        Key keyFromMethod = jwtService.getSignKey();

        // When
        assertEquals(keyFromMethod,key);
    }

    @Test
    @DisplayName("Create Token")
    public void testCreateToken() throws NoSuchFieldException, IllegalAccessException {
        // Given
        Long id = 1L;

        // Manually set the SECRET field using reflection
        Field secretField = jwtService.getClass().getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        Map<String, Object> claims = new HashMap<>();

        String token = jwtService.createToken(claims,id);

        // When
        assertNotNull(token);
    }
    @Test
    @DisplayName("Test Extract Username")
    public void testExtractUsername() throws NoSuchFieldException, IllegalAccessException {
        // Manually set the SECRET field using reflection
        Field secretField = jwtService.getClass().getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        // Generate a token with a future expiration date
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin"); // Add additional claim
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject("1")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtService.getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        // When
        String username = jwtService.extractUsername(token);

        // Then
        assertNotNull(username);
        assertEquals("1", username);
    }

    @Test
    @DisplayName("Test Extract Expiration")
    public void testExtractExpiration() throws NoSuchFieldException, IllegalAccessException {
        // Manually set the SECRET field using reflection
        Field secretField = jwtService.getClass().getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        // Generate a token with a future expiration date
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin"); // Add additional claim
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject("1")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtService.getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        // When
        Date expirationDate = jwtService.extractExpiration(token);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        assertNotNull(expirationDate);
        assertEquals(sdf.format(expiryDate), sdf.format(expirationDate));
    }

    @Test
    @DisplayName("Test Extract Claim")
    public void testExtractClaim() throws NoSuchFieldException, IllegalAccessException {
        // Manually set the SECRET field using reflection
        Field secretField = jwtService.getClass().getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        // Generate a token with a future expiration date
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "admin"); // Add additional claim
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject("1")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtService.getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        // When
        String subject = jwtService.extractClaim(token, Claims::getSubject);
        Date expirationDate = jwtService.extractClaim(token, Claims::getExpiration);
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);
        // Then
        assertNotNull(subject);
        assertEquals("1", subject);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        assertNotNull(expirationDate);
        assertEquals(sdf.format(expiryDate), sdf.format(expirationDate));

        assertNotNull(issuedAt);
        assertEquals(sdf.format(now), sdf.format(issuedAt));
    }


    @Test
    @DisplayName("Test Validate Token")
    public void testValidateToken() throws NoSuchFieldException, IllegalAccessException {
        // Manually set the SECRET field using reflection
        Field secretField = jwtService.getClass().getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secretField.set(jwtService, secret);

        // Given
        User userDetails = new User(1L,"test-name", 18, "test@gmail.com",passwordEncoder.encode("123"));
        // Generate a token with a future expiration date
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);
        Map<String, Object> claims = new HashMap<>();
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject("1")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtService.getSignKey(), SignatureAlgorithm.HS256)
                .compact();


        // When
        boolean isValid = jwtService.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }




}
