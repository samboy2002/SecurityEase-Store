package com.example.store.repository;

import com.example.store.entity.Order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 20);
    }

    @Test
    void testFindAllWithCustomers() {
        Page<Order> results = orderRepository.findAllWithCustomers(pageable);
        assertThat(results).isNotEmpty();

        results.forEach(o -> assertThat(o.getCustomer()).isNotNull());
    }

    @Test
    void testFindByIdWithCustomers() {
        Optional<Order> order = orderRepository.findByIdWithCustomers(1L);
        assertThat(order).isPresent();
        assertThat(order.get().getCustomer()).isNotNull();
    }

    @Test
    void testFindByIdWithCustomers_InvalidedId() {
        Optional<Order> order = orderRepository.findByIdWithCustomers(99999L);
        assertThat(order).isNotPresent();
    }
}
