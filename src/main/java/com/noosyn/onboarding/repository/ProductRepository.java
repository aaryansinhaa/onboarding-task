package com.noosyn.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noosyn.onboarding.entity.Product;

/**
 * Repository interface for performing CRUD operations on {@link Product} entities.
 * <p>
 * This interface leverages Spring Data JPA to automatically generate
 * standard database operations such as saving, updating, deleting,
 * and querying products.
 * </p>
 *
 * <p>No implementation is required; Spring generates it at runtime.</p>
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
