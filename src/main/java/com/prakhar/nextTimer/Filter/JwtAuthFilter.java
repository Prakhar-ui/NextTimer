package com.prakhar.nextTimer.Filter;

import com.prakhar.nextTimer.Exception.CustomAuthorizationException;
import com.prakhar.nextTimer.Service.JwtService;
import com.prakhar.nextTimer.Service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            try {
                String authHeader = request.getHeader("Authorization");
                String token = null;
                String username = null;

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                    username = jwtService.extractUsername(token);
                }

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        UserDetails userDetails = userDetailService.loadUserByUsername(username);

                        if (jwtService.validateToken(token, userDetails)) {
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    } catch (UsernameNotFoundException e) {
                        // Handle the case where the user details are not found
                        logger.error("User details not found for username: " + username, e);
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

