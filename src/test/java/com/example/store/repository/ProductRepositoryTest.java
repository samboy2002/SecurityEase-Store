package com.example.store.repository;

import com.example.store.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = ProductRepository.class)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAllWithOrders() {
        Page<Product> results = productRepository.findAllWithOrders(PageRequest.of(0, 20));
        assertThat(results).isNotEmpty();

        results.forEach(p -> assertThat(p.getOrders()).isNotEmpty());
    }

    @Test
    void findByIdWithOrders() {
        Optional<Product> product = productRepository.findByIdWithOrders(1L);
        assertThat(product).isPresent();
        assertThat(product.get().getOrders()).isNotEmpty();
    }
}