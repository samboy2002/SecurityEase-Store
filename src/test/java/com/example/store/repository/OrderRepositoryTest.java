package com.example.store.repository;

import com.example.store.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ComponentScan(basePackageClasses = OrderRepository.class)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findAllWithCustomers() {
        List<Order> results = orderRepository.findAllWithCustomers();
        assertNotNull(results);
        assertTrue(results.size() > 0);

        results.forEach(o -> assertNotNull(o.getCustomer()));
    }

    @Test
    void findByIdWithCustomers() {
        Optional<Order> order = orderRepository.findByIdWithCustomers(1L);
        assertTrue(order.isPresent());
        assertNotNull(order.get().getCustomer());
    }

    @Test
    void findByIdWithCustomers_InvalidedId() {
        Optional<Order> order = orderRepository.findByIdWithCustomers(99999L);
        assertFalse(order.isPresent());
    }
}