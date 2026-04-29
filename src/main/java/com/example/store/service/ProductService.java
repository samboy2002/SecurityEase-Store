package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.request.ProductCreateRequest;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDTO createProduct(ProductCreateRequest request) {
        log.info("Creating product: {}", request.getDescription());

        Product newProduct = new Product();
        newProduct.setDescription(request.getDescription());
        Product savedProduct = productRepository.save(newProduct);

        return productMapper.productToProductDTO(savedProduct);
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        log.debug("Fetching all products with pagination: {}", pageable);

        if (pageable.isUnpaged()) {
            pageable = PageRequest.of(0, 20);
        }

        Page<Product> products = productRepository.findAllWithOrders(pageable);

        return products.map(productMapper::productToProductDTO);
    }

    public Optional<ProductDTO> getProductById(Long id) {
        log.debug("Fetching product by id: {}", id);

        return productRepository.findByIdWithOrders(id).map(productMapper::productToProductDTO);
    }
}
