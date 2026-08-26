package com.EDebugger.booking_logic.services;

import com.EDebugger.booking_logic.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.EDebugger.booking_logic.repositories.UserRepository;


@Service
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(name);
                //.orElseThrow(() -> new UsernameNotFoundException("User not found: " + name));

        // Convert the user's enum role to Spring Security format
        String authority = "ROLE_" + user.getUserRole().name();  // e.g., "ROLE_ADMIN", "ROLE_USER"

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserName())
                .password(user.getPassword())
                .authorities(authority)  // Use the actual role from the entity
                .build();
    }
}
