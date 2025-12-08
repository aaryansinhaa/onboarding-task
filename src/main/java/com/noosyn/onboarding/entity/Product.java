package com.noosyn.onboarding.entity;

import java.math.BigDecimal;

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
 * Represents a product available within the onboarding system.
 * <p>
 * This entity stores basic product information such as name and price,
 * and is persisted to the database via JPA.
 * </p>
 *
 * <p>Fields include:</p>
 * <ul>
 *   <li>{@code id} – Primary key</li>
 *   <li>{@code name} – Display name of the product</li>
 *   <li>{@code price} – Monetary price of the product</li>
 * </ul>
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /**
     * Primary key identifier for the product.
     * <p>
     * Generated automatically using the identity strategy.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Human-readable name of the product.
     */
    private String name;

    /**
     * Monetary price of the product.
     * <p>
     * Represented as {@link BigDecimal} to avoid floating-point precision issues.
     * </p>
     */
    private BigDecimal price;
}
