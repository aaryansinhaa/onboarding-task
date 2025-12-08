package com.noosyn.onboarding.dto.product_dto;

import java.math.BigDecimal;

/**
 * Request DTO used for creating or updating a product.
 * <p>
 * Contains the product's name and price as provided by the client.
 * </p>
 *
 * @param name  the name of the product
 * @param price the price of the product, represented as {@link BigDecimal}
 */
public record ProductRequest(String name, BigDecimal price) {}
