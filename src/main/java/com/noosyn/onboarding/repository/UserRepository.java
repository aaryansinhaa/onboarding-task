package com.noosyn.onboarding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noosyn.onboarding.entity.User;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Extends {@link JpaRepository} to provide built-in CRUD operations as well as
 * pagination and sorting functionality. Spring Data JPA automatically generates
 * the implementation at runtime.
 * </p>
 *
 * <p>This repository also declares custom query methods for user lookups.</p>
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by its username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the matching {@code User}, or empty if none found
     */
    Optional<User> findByUsername(String username);
}
