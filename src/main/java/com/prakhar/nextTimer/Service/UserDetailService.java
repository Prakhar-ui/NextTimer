package com.prakhar.nextTimer.Service;

import com.prakhar.nextTimer.Entity.User;
import com.prakhar.nextTimer.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            Optional<User> userOptional = userRepository.findByEmail(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                        String.valueOf(user.getId()),
                        user.getPassword(),
                        user.getAuthorities());

                logger.info("User with email '{}' found successfully and UserDetails returned", username);

                return userDetails;
            } else {
                logger.error("User with email '{}' not found", username);
                throw new UsernameNotFoundException("User not found");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format", e);
            throw new UsernameNotFoundException("Invalid user ID format");
        }
    }
}
