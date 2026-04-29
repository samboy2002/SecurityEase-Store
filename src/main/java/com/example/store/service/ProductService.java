package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import com.example.store.mapper.ProductMapper;
import com.example.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDTO createProduct(Product product) {
        log.info("Creating product: {}", product.getDescription());

        return productMapper.productToProductDTO(productRepository.save(product));
    }

    public List<ProductDTO> getAllProducts() {
        log.debug("Fetching all products.");

        List<Product> products = productRepository.findAllWithOrders();

        return productMapper.productsToProductDTOs(products);
    }

    public Optional<ProductDTO> getProductById(Long id) {
        log.debug("Fetching product by id: {}", id);

        return productRepository.findByIdWithOrders(id)
                                .map(productMapper::productToProductDTO);
    }
}
