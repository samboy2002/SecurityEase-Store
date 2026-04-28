package com.example.store.repository;

import com.example.store.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = CustomerRepository.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByNameContainingIgnoreCase() {
        List<Customer> results = customerRepository.findByNameContainingIgnoreCaseWithOrders("John");
        assertNotNull(results);
        assertEquals(3, results.size());

        results.forEach(c -> assertTrue(c.getOrders().size() > 0));

        assertTrue(results.stream().anyMatch(c -> c.getName().equals("Moses Johnson")));
        assertTrue(results.stream().anyMatch(c -> c.getName().equals("Johnathan Mayer I")));
        assertTrue(results.stream().anyMatch(c -> c.getName().equals("Johnny Roob")));
    }
}