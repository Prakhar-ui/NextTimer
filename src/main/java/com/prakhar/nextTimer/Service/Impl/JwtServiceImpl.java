package com.prakhar.nextTimer.Service.Impl;

import com.prakhar.nextTimer.Service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Transactional
public class JwtServiceImpl implements JwtService{

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    public String generateToken(Long id) {
        try {
            Map<String, Object> claims = new HashMap<>();
            return createToken(claims, id);
        } catch (JwtException e) {
            logger.error("Error generating token", e);
            throw e;
        }
    }

    public Key getSignKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET);
            Key signingKey = Keys.hmacShaKeyFor(keyBytes);
            return signingKey;
        } catch (Exception e) {
            logger.error("Error getting signing key", e);
            throw new JwtException("Error getting signing key", e);
        }
    }

    protected String createToken(Map<String, Object> claims, Long id) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationMs);
            return Jwts.builder().setClaims(claims).setSubject(String.valueOf(id)).setIssuedAt(now).setExpiration(expiryDate).signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        } catch (JwtException e) {
            logger.error("Error creating token", e);
            throw e;
        }
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            logger.error("Error extracting username from token", e);
            throw e;
        }
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (JwtException e) {
            logger.error("Error extracting expiration date from token", e);
            throw e;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (JwtException e) {
            logger.error("Error extracting claim from token", e);
            throw e;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            logger.error("Error extracting all claims from token", e);
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (JwtException e) {
            logger.error("Error checking if token is expired", e);
            throw e;
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String id = extractUsername(token);
            boolean isValid = (id.equals(userDetails.getUsername()) && !isTokenExpired(token));
            if (isValid) {
                logger.info("Token validation successful for user with id: {}", id);
            } else {
                logger.info("Token validation failed for user with id: {}", id);
            }
            return isValid;
        } catch (JwtException e) {
            logger.error("Error validating token", e);
            throw e;
        }
    }
}
