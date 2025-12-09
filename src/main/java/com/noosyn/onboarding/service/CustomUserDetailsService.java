package com.noosyn.onboarding.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.noosyn.onboarding.entity.User;
import com.noosyn.onboarding.exception.AppException;
import com.noosyn.onboarding.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link UserDetailsService} used by Spring Security
 * to load user-specific authentication information.
 * <p>
 * This service retrieves {@link User} entities from the database and
 * converts them into Spring Security-compatible {@link UserDetails}
 * objects for authentication and authorization processing.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    /**
     * Loads a user by its username and maps it into a {@link UserDetails} object.
     * <p>
     * The method:
     * </p>
     * <ul>
     * <li>Searches the database for a matching user</li>
     * <li>Throws {@link UsernameNotFoundException} if no match is found</li>
     * <li>Builds a Spring Security
     * {@link org.springframework.security.core.userdetails.User}
     * instance with username, password, and roles</li>
     * </ul>
     *
     * @param username the username to look up
     * @return a {@link UserDetails} instance representing the authenticated user
     * @throws UsernameNotFoundException if the username is not found in the system
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new AppException("ERR-103"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
