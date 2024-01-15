package com.prakhar.nextTimer.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    public String SECRET;

    @Value("${jwt.expirationMs}")
    private long expirationMs;

    public String generateToken(Long id) {
        try {
            Map<String, Object> claims = new HashMap<>();
            String token = createToken(claims, id);
            logger.info("Token generated successfully for user with id: {}", id);
            return token;
        } catch (Exception e) {
            logger.error("Error generating token", e);
            throw e;
        }
    }

    private Key getSignKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET);
            Key key = Keys.hmacShaKeyFor(keyBytes);
            logger.info("Getting Sign Key success");
            return key;
        } catch (Exception e) {
            logger.error("Error getting signing key", e);
            throw e;
        }
    }

    private String createToken(Map<String, Object> claims, Long id) {
        try {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expirationMs);

            String token = Jwts.builder().setClaims(claims).setSubject(String.valueOf(id)).setIssuedAt(now).setExpiration(expiryDate).signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

            logger.info("Token created successfully for user with id: {}", id);
            return token;
        } catch (Exception e) {
            logger.error("Error creating token", e);
            throw e;
        }
    }

    public String extractUsername(String token) {
        try {
            String extractedUsername = extractClaim(token, Claims::getSubject);
            logger.info("Username Successfully Extracted");
            return extractedUsername;
        } catch (Exception e) {
            logger.error("Error extracting username from token", e);
            throw e;
        }
    }

    public Date extractExpiration(String token) {
        try {
            Date expirationDate =  extractClaim(token, Claims::getExpiration);
            logger.info("ExpirationDate Successfully Extracted");
            return expirationDate;
        } catch (Exception e) {
            logger.error("Error extracting expiration date from token", e);
            throw e;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            T resolvedClaims = claimsResolver.apply(claims);
            logger.info("Claims Successfully Extracted");
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.error("Error extracting claim from token", e);
            throw e;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
            logger.info("All Claims Successfully Extracted");
            return claims;
        } catch (Exception e) {
            logger.error("Error extracting all claims from token", e);
            throw e;
        }
    }

    private Boolean isTokenExpired(String token) {
        try {
            Boolean expired = extractExpiration(token).before(new Date());
            logger.info("Token Expiry check");
            return expired;
        } catch (Exception e) {
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
        } catch (Exception e) {
            logger.error("Error validating token", e);
            throw e;
        }
    }
}


