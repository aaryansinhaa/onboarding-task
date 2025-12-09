package com.noosyn.onboarding.service;

import com.noosyn.onboarding.dto.product_dto.ProductRequest;
import com.noosyn.onboarding.dto.product_dto.ProductResponse;
import com.noosyn.onboarding.entity.Product;
import com.noosyn.onboarding.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- CREATE ----------
    @Test
    void testCreateProduct() {
        ProductRequest req = new ProductRequest("Laptop", BigDecimal.valueOf(50000));

        Product saved = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(50000))
                .build();

        when(repo.save(any(Product.class))).thenReturn(saved);

        ProductResponse resp = service.create(req);

        assertEquals(1L, resp.id());
        assertEquals("Laptop", resp.name());
        assertEquals(BigDecimal.valueOf(50000), resp.price());
        verify(repo).save(any(Product.class));
    }

    // ---------- GET ALL ----------
    @Test
    void testGetAllProducts() {
        List<Product> products = List.of(
                Product.builder().id(1L).name("Laptop").price(BigDecimal.valueOf(50000)).build(),
                Product.builder().id(2L).name("Phone").price(BigDecimal.valueOf(20000)).build()
        );

        when(repo.findAll()).thenReturn(products);

        Page<Product> resp = service.getAllProducts(0, 10);

        assertEquals(2, resp.getContent().size());
        assertEquals("Laptop", resp.getContent().get(0).getName());
        verify(repo).findAll();
    }

    // ---------- GET ONE ----------
    @Test
    void testGetProduct() {
        Product p = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(50000))
                .build();

        when(repo.findById(1L)).thenReturn(Optional.of(p));

        ProductResponse resp = service.get(1L);

        assertEquals(1L, resp.id());
        assertEquals("Laptop", resp.name());
        verify(repo).findById(1L);
    }

    @Test
    void testGetProduct_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.get(1L));
    }

    // ---------- UPDATE ----------
    @Test
    void testUpdateProduct() {
        Product existing = Product.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(50000))
                .build();

        ProductRequest req = new ProductRequest("Laptop Pro", BigDecimal.valueOf(75000));

        when(repo.findById(1L)).thenReturn(Optional.of(existing));

        ProductResponse resp = service.update(1L, req);

        assertEquals("Laptop Pro", resp.name());
        assertEquals(BigDecimal.valueOf(75000), resp.price());
        verify(repo).save(existing);
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(repo.findById(1L)).thenReturn(Optional.empty());

        ProductRequest req = new ProductRequest("Laptop", BigDecimal.valueOf(50000));

        assertThrows(RuntimeException.class, () -> service.update(1L, req));
    }

    // ---------- DELETE ----------
    @Test
    void testDeleteProduct() {
        doNothing().when(repo).deleteById(1L);

        service.delete(1L);

        verify(repo).deleteById(1L);
    }
}
