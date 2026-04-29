package com.example.store.repository;

import com.example.store.entity.Customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testFindByNameContainingIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 20);

        Page<Customer> results = customerRepository.findByNameContainingIgnoreCaseWithOrders("John", pageable);
        assertThat(results).isNotNull();
        assertThat(results.getTotalElements()).isEqualTo(3L);

        results.forEach(c -> assertThat(c.getOrders()).isNotEmpty());

        assertThat(results.getContent()).extracting(Customer::getName).contains("Moses Johnson");
        assertThat(results.getContent()).extracting(Customer::getName).contains("Johnathan Mayer I");
        assertThat(results.getContent()).extracting(Customer::getName).contains("Johnny Roob");
    }
}
