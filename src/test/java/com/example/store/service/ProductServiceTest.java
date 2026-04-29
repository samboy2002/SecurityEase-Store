package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.request.ProductCreateRequest;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setDescription("Test Product");

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setDescription("Test Product");
        productDTO.setOrderIds(List.of());

        pageable = PageRequest.of(0, 20);
    }

    @Test
    void testCreateProduct() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setDescription("Test Product");

        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.createProduct(request);

        assertThat(result).isEqualTo(productDTO);
    }

    @Test
    void testGetAllProducts() {
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAllWithOrders(pageable)).thenReturn(page);
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        Page<ProductDTO> results = productService.getAllProducts(pageable);
        assertThat(results).isNotEmpty();
        assertThat(results.getContent()).containsExactly(productDTO);
    }

    @Test
    void testGetAllProducts_unpaged() {
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAllWithOrders(pageable)).thenReturn(page);
        when(productMapper.productToProductDTO(product)).thenReturn(productDTO);

        Page<ProductDTO> results = productService.getAllProducts(Pageable.unpaged());
        assertThat(results).isNotEmpty();
        assertThat(results.getContent()).containsExactly(productDTO);
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