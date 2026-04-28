package com.example.store.repository;

import com.example.store.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = ProductRepository.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAllWithOrders() {
        List<Product> results = productRepository.findAllWithOrders();
        assertNotNull(results);
        assertFalse(results.isEmpty());

        results.forEach(p -> assertFalse(p.getOrders().isEmpty()));
    }

    @Test
    void findByIdWithOrders() {
        Optional<Product> product = productRepository.findByIdWithOrders(1L);
        assertTrue(product.isPresent());
        assertFalse(product.get().getOrders().isEmpty());
    }
}