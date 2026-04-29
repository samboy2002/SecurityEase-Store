package com.example.store.repository;

import com.example.store.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = CustomerRepository.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findByNameContainingIgnoreCase() {
        List<Customer> results = customerRepository.findByNameContainingIgnoreCaseWithOrders("John");
        assertThat(results).isNotNull();
        assertThat(results.size()).isEqualTo(3);

        results.forEach(c -> assertThat(c.getOrders()).isNotEmpty());

        assertThat(results).extracting(Customer::getName).contains("Moses Johnson");
        assertThat(results).extracting(Customer::getName).contains("Johnathan Mayer I");
        assertThat(results).extracting(Customer::getName).contains("Johnny Roob");
    }
}