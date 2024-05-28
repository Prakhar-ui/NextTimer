package com.prakhar.nextTimer.Filter;

import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.UserRepository;
import com.prakhar.nextTimer.Service.JwtService;
import com.prakhar.nextTimer.Service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String userId = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userId = jwtService.extractUsername(token); // Change this line
            }

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));

                    UserDetails userDetails = userDetailService.loadUserByUsername(userOptional.get().getEmail()); // Change this line
                    if (jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.info("User authenticated successfully for user ID: {}", userId);

                    } else {
                        logger.warn("JWT token validation failed for user ID: {}", userId);
                    }
                } catch (UsernameNotFoundException e) {
                    // Handle the case where the user details are not found
                    logger.error("User details not found for user ID: " + userId, e);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // Handle any unexpected exceptions during the filter execution
            logger.error("Exception in authentication filter", e);
            throw new ServletException("Exception in authentication filter", e);
        }
    }

}

