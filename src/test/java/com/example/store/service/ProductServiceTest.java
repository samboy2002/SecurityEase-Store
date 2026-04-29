package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setDescription("Test Product");
        productDTO.setOrderIds(List.of());
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.createProduct(product);

        assertThat(result).isEqualTo(productDTO);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAllWithOrders()).thenReturn(List.of(product));
        when(productMapper.productsToProductDTOs(List.of(product))).thenReturn(List.of(productDTO));

        List<ProductDTO> results = productService.getAllProducts();
        assertThat(results).isNotEmpty();
        assertThat(results).containsExactly(productDTO);
    }

    @Test
    void testGetProductById() {
        when(productRepository.findByIdWithOrders(1L)).thenReturn(Optional.of(product));
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        Optional<ProductDTO> result = productService.getProductById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(productDTO);
    }

    @Test
    void testGetProductById_withInvalidedId() {
        when(productRepository.findByIdWithOrders(999L)).thenReturn(Optional.empty());

        Optional<ProductDTO> result = productService.getProductById(999L);

        assertThat(result).isNotPresent();
    }
}