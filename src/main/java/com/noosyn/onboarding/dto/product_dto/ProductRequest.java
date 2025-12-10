package com.noosyn.onboarding.dto.product_dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO used for creating or updating a product.
 * <p>
 * Contains the product's name and price as provided by the client.
 * </p>
 *
 * @param name  the name of the product
 * @param price the price of the product, represented as {@link BigDecimal}
 */
public record ProductRequest(
    @NotBlank(message = "ERR-200")
    String name,

    @NotNull(message = "ERR-200")
    @DecimalMin(value = "0.0", inclusive = true, message = "ERR-200")
    BigDecimal price
) {}
