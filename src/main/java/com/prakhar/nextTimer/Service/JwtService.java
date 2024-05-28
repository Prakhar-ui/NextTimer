package com.prakhar.nextTimer.Service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    String generateToken(Long id);

    Boolean validateToken(String token, UserDetails userDetails);

    String extractUsername(String token);

    Key getSignKey();

    Date extractExpiration(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
