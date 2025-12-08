package com.noosyn.onboarding.dto.product_dto;

import java.math.BigDecimal;

/**
 * Response DTO representing product information returned to the client.
 * <p>
 * Includes the product's identifier, name, and price. Used to ensure
 * API responses remain stable and decoupled from internal entity models.
 * </p>
 *
 * @param id    the unique identifier of the product
 * @param name  the name of the product
 * @param price the price of the product, represented as {@link BigDecimal}
 */
public record ProductResponse(Long id, String name, BigDecimal price) {}
