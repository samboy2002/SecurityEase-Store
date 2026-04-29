package com.example.store.repository;

import com.example.store.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = OrderRepository.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findAllWithCustomers() {
        List<Order> results = orderRepository.findAllWithCustomers();
        assertThat(results).isNotNull();
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