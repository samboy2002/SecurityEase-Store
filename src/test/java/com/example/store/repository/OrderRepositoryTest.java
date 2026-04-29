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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 20);
    }

    @Test
    void findAllWithCustomers() {
        Page<Order> results = orderRepository.findAllWithCustomers(pageable);
        assertThat(results).isNotEmpty();

        results.forEach(o -> assertThat(o.getCustomer()).isNotNull());
    }

    @Test
    void findByIdWithCustomers() {
        Optional<Order> order = orderRepository.findByIdWithCustomers(1L);
        assertThat(order).isPresent();
        assertThat(order.get().getCustomer()).isNotNull();
    }

    @Test
    void findByIdWithCustomers_InvalidedId() {
        Optional<Order> order = orderRepository.findByIdWithCustomers(99999L);
        assertThat(order).isNotPresent();
    }
}