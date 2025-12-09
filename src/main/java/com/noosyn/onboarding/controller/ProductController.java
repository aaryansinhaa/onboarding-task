package com.noosyn.onboarding.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.noosyn.onboarding.dto.product_dto.PaginatedResponse;
import com.noosyn.onboarding.dto.product_dto.ProductRequest;
import com.noosyn.onboarding.dto.product_dto.ProductResponse;
import com.noosyn.onboarding.entity.Product;
import com.noosyn.onboarding.service.ProductService;
import com.noosyn.onboarding.utils.ApiEndPointConstants;

import lombok.RequiredArgsConstructor;

/**
 * REST controller providing CRUD operations for managing products.
 * <p>
 * Exposes endpoints under the {@code /products} path and delegates all business
 * logic to {@link ProductService}. Responses are wrapped using DTOs to ensure
 * clear separation between API models and internal entities.
 * </p>
 */
@RestController
@RequestMapping(ApiEndPointConstants.PRODUCT_BASE)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    /**
     * Creates a new product.
     *
     * @param req the request payload containing product creation data
     * @return a {@link ResponseEntity} with the created product details
     */
    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    /**
     * Retrieves all products.
     *
     * @return a {@link ResponseEntity} containing a list of products
     */
    @GetMapping
    public ResponseEntity<PaginatedResponse<ProductResponse>> getProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
    org.springframework.data.domain.Page<Product> productPage = service.getAllProducts(page, size);
    List<ProductResponse> items = productPage.getContent().stream()
            .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice()))
            .toList();

     PaginatedResponse<ProductResponse> response = new PaginatedResponse<>(
            items,
            productPage.getNumber(),
            productPage.getTotalElements(),
            productPage.getTotalPages()
    );

    return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a single product by its identifier.
     *
     * @param id the ID of the product to retrieve
     * @return a {@link ResponseEntity} with the product details
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    /**
     * Updates an existing product.
     *
     * @param id  the ID of the product to update
     * @param req the updated product data
     * @return a {@link ResponseEntity} with the updated product information
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @RequestBody ProductRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    /**
     * Deletes a product by its identifier.
     *
     * @param id the ID of the product to delete
     * @return a {@link ResponseEntity} with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
