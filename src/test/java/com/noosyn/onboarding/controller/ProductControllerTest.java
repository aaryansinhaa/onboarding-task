package com.noosyn.onboarding.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noosyn.onboarding.dto.product_dto.ProductRequest;
import com.noosyn.onboarding.dto.product_dto.ProductResponse;
import com.noosyn.onboarding.service.ProductService;
import com.noosyn.onboarding.utils.ApiEndPointConstants;
import com.noosyn.onboarding.utils.JwtAuthenticationFilter;
import com.noosyn.onboarding.utils.JwtUtils;
import com.noosyn.onboarding.utils.SecurityConfig;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = ProductController.class,
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {
                JwtUtils.class,
                JwtAuthenticationFilter.class,
                SecurityConfig.class
            }
        )
    }
)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- CREATE ----------
    @Test
    void testCreateProduct() throws Exception {
        ProductRequest req = new ProductRequest("Laptop", new BigDecimal("50000.0"));
        ProductResponse resp = new ProductResponse(1L, "Laptop", new BigDecimal("50000.0"));

        when(productService.create(req)).thenReturn(resp);

        mockMvc.perform(post(ApiEndPointConstants.PRODUCT_BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(50000.0));

        verify(productService).create(req);
    }

    // ---------- GET ALL ----------
    @Test
    void testGetAllProducts() throws Exception {
        Page<ProductResponse> products = mock(Page.class);
        when(products.getContent()).thenReturn(List.of(
                new ProductResponse(1L, "Laptop", new BigDecimal("50000.0")),
                new ProductResponse(2L, "Phone", new BigDecimal("20000.0"))
        ));
        when(products.getNumber()).thenReturn(0);
        when(products.getTotalElements()).thenReturn(2L);
        when(products.getTotalPages()).thenReturn(1); 

        when(productService.getAllProducts(0, 10)).thenReturn((Page) products);

        mockMvc.perform(get(ApiEndPointConstants.PRODUCT_BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.size()").value(2));

        verify(productService).getAllProducts(0, 10);
    }

    // ---------- GET ONE ----------
    @Test
    void testGetProduct() throws Exception {
        ProductResponse resp = new ProductResponse(1L, "Laptop", new BigDecimal("50000.0"));

        when(productService.get(1L)).thenReturn(resp);

        mockMvc.perform(get(ApiEndPointConstants.PRODUCT_BASE + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(productService).get(1L);
    }

    // ---------- UPDATE ----------
    @Test
    void testUpdateProduct() throws Exception {
        ProductRequest req = new ProductRequest("Laptop Pro", new BigDecimal("75000.0"));
        ProductResponse resp = new ProductResponse(1L, "Laptop Pro", new BigDecimal("75000.0"));

        when(productService.update(1L, req)).thenReturn(resp);

        mockMvc.perform(put(ApiEndPointConstants.PRODUCT_BASE + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop Pro"));

        verify(productService).update(1L, req);
    }

    // ---------- DELETE ----------
    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).delete(1L);

        mockMvc.perform(delete(ApiEndPointConstants.PRODUCT_BASE + "/1"))
                .andExpect(status().isNoContent());

        verify(productService).delete(1L);
    }
}
