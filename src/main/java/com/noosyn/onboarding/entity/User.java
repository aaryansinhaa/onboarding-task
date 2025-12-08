package com.noosyn.onboarding.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an application user within the onboarding system.
 * <p>
 * This entity stores authentication credentials and authorization role
 * information. It is persisted in the underlying database via JPA.
 * </p>
 *
 * <p>Fields include:</p>
 * <ul>
 *   <li>{@code id} – Primary key</li>
 *   <li>{@code username} – Unique username used for login</li>
 *   <li>{@code password} – Encrypted password</li>
 *   <li>{@code role} – User role (e.g., {@code "USER"}, {@code "ADMIN"})</li>
 * </ul>
 *
 * Lombok annotations generate boilerplate code such as getters, setters,
 * constructors, and builder pattern implementation.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /**
     * Primary key identifier for the user.
     * <p>
     * Generated automatically using the identity strategy.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username used to authenticate the user.
     * Typically unique within the system.
     */
    private String username;

    /**
     * Encrypted password associated with the user.
     * Should never be stored or logged in plain text.
     */
    private String password;

    /**
     * Role assigned to the user, determining authorization level.
     * Expected values include {@code "USER"} or {@code "ADMIN"}.
     */
    private String role;
}
