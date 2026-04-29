package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import com.example.store.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        productDTO = new ProductDTO();
        productDTO.setDescription("Test Product");
        productDTO.setId(1L);
        productDTO.setOrderIds(List.of());
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any())).thenReturn(productDTO);

        mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
                                         .content(objectMapper.writeValueAsString(productDTO)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.description").value("Test Product"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Page<ProductDTO> page = new PageImpl<>(List.of(productDTO), pageable, 1);

        when(productService.getAllProducts(pageable)).thenReturn(page);

        mockMvc.perform(get("/products"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].description").value("Test Product"));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(productDTO));

        mockMvc.perform(get("/products/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.description").value("Test Product"));
    }

    @Test
    void testGetProductById_WithInvalidedId_ReturnStatusCode404() throws Exception {
        when(productService.getProductById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/999"))
               .andExpect(status().isNotFound());
    }
}