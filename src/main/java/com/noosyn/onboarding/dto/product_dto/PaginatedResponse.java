package com.noosyn.onboarding.dto.product_dto;

import java.util.List;

public record PaginatedResponse<T>(
        List<T> items,
        int currentPage,
        long totalItems,
        int totalPages
) {}
