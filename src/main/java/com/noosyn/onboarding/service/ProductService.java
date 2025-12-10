package com.noosyn.onboarding.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.noosyn.onboarding.dto.product_dto.PaginatedResponse;
import com.noosyn.onboarding.dto.product_dto.ProductRequest;
import com.noosyn.onboarding.dto.product_dto.ProductResponse;
import com.noosyn.onboarding.entity.Product;
import com.noosyn.onboarding.exception.AppException;
import com.noosyn.onboarding.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service layer responsible for managing product-related operations.
 * <p>
 * Provides methods for creating, retrieving, updating, and deleting
 * {@link Product} entities. This service converts entities into DTOs
 * to ensure a clean separation between persistence and API models.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    /**
     * Creates a new product based on the provided request data.
     *
     * @param req the product creation request containing name and price
     * @return a {@link ProductResponse} representing the newly created product
     */
    public ProductResponse create(ProductRequest req) {
        Product p = repo.save(Product.builder()
                .name(req.name())
                .price(req.price())
                .build());
        return new ProductResponse(p.getId(), p.getName(), p.getPrice());
    }

    /**
     * Retrieves all products from the system.
     *
     * @param page the page number (0-based)
     * @param size the page size
     * @return a page of {@link Product} entities
     */
    public PaginatedResponse<ProductResponse> getAllProducts(int page, int size) {

        PageRequest pageable = PageRequest.of(page, size);

        Page<Product> productPage = repo.findAll(pageable);

        List<ProductResponse> items = productPage.getContent()
                .stream()
                .map(p -> new ProductResponse(
                        p.getId(),
                        p.getName(),
                        p.getPrice()))
                .toList();

        return new PaginatedResponse<>(
                items,
                productPage.getNumber(),
                productPage.getTotalElements(),
                productPage.getTotalPages());
    }

    /**
     * Retrieves a product by its identifier.
     *
     * @param id the ID of the product to retrieve
     * @return a {@link ProductResponse} containing product details
     * @throws RuntimeException if no product exists with the given ID
     */
    public ProductResponse get(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new AppException("ERR-201"));
        return new ProductResponse(p.getId(), p.getName(), p.getPrice());
    }

    /**
     * Updates an existing product with new information.
     *
     * @param id  the ID of the product to update
     * @param req the updated product data
     * @return a {@link ProductResponse} with the updated fields
     * @throws RuntimeException if the product does not exist
     */
    public ProductResponse update(Long id, ProductRequest req) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new AppException("ERR-201"));

        p.setName(req.name());
        p.setPrice(req.price());
        repo.save(p);

        return new ProductResponse(p.getId(), p.getName(), p.getPrice());
    }

    /**
     * Deletes a product by its identifier.
     *
     * @param id the ID of the product to delete
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
