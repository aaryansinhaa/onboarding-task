package com.noosyn.onboarding.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noosyn.onboarding.dto.product_dto.PaginatedResponse;
import com.noosyn.onboarding.dto.product_dto.ProductRequest;
import com.noosyn.onboarding.dto.product_dto.ProductResponse;
import com.noosyn.onboarding.exception.AppException;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class, excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                                JwtUtils.class,
                                JwtAuthenticationFilter.class,
                                SecurityConfig.class
                })
})
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
        void ShouldCreateProduct() throws Exception {
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

        @Test
        void ShouldFailCreateProductWhenInvalidInput() throws Exception {
                ProductRequest req = new ProductRequest("", new BigDecimal("-100.0"));

                mockMvc.perform(post(ApiEndPointConstants.PRODUCT_BASE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                                .andExpect(status().isBadRequest());

                verify(productService, never()).create(any(ProductRequest.class));
        }
        // ---------- GET ALL ----------
        @Test
        void ShouldGetAllProducts() throws Exception {

                PaginatedResponse<ProductResponse> response = new PaginatedResponse<>(
                                List.of(
                                                new ProductResponse(1L, "Laptop", new BigDecimal("50000.0")),
                                                new ProductResponse(2L, "Phone", new BigDecimal("20000.0"))),
                                0,
                                2L,
                                1);

                when(productService.getAllProducts(0, 10)).thenReturn(response);

                mockMvc.perform(get(ApiEndPointConstants.PRODUCT_BASE))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.items.size()").value(2))
                                .andExpect(jsonPath("$.currentPage").value(0))
                                .andExpect(jsonPath("$.totalItems").value(2))
                                .andExpect(jsonPath("$.totalPages").value(1));

                verify(productService).getAllProducts(0, 10);
        }

        // ---------- GET ONE ----------
        @Test
        void ShouldGetProduct() throws Exception {
                ProductResponse resp = new ProductResponse(1L, "Laptop", new BigDecimal("50000.0"));

                when(productService.get(1L)).thenReturn(resp);

                mockMvc.perform(get(ApiEndPointConstants.PRODUCT_BASE + "/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1));

                verify(productService).get(1L);
        }

        @Test
        void ShouldFailGetProductWhenProductNotFound() throws Exception {
                when(productService.get(99L)).thenThrow(new AppException("ERR-201"));

                mockMvc.perform(get(ApiEndPointConstants.PRODUCT_BASE + "/99"))
                                .andExpect(status().isBadRequest());

                verify(productService).get(99L);
        }

        // ---------- UPDATE ----------
        @Test
        void ShouldUpdateProduct() throws Exception {
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

        @Test
        void ShouldFailUpdateProductWhenProductNotFound() throws Exception {
                ProductRequest req = new ProductRequest("Laptop Pro", new BigDecimal("75000.0"));
                when(productService.update(99L, req)).thenThrow(new AppException("ERR-201"));
                mockMvc.perform(put(ApiEndPointConstants.PRODUCT_BASE + "/99")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                                .andExpect(status().isBadRequest());
                verify(productService).update(99L, req);
        }

        // ---------- DELETE ----------
        @Test
        void ShouldDeleteProduct() throws Exception {
                doNothing().when(productService).delete(1L);

                mockMvc.perform(delete(ApiEndPointConstants.PRODUCT_BASE + "/1"))
                                .andExpect(status().isNoContent());

                verify(productService).delete(1L);
        }

        @Test
        void ShouldFailDeleteProductWhenProductNotFound() throws Exception {
                doThrow(new AppException("ERR-201")).when(productService).delete(99L);

                mockMvc.perform(delete(ApiEndPointConstants.PRODUCT_BASE + "/99"))
                                .andExpect(status().isBadRequest());

                verify(productService).delete(99L);
        }
}
