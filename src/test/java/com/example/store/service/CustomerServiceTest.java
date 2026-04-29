package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");
        customer.setId(1L);

        customerDTO = new CustomerDTO();
        customerDTO.setName("John Doe");
        customerDTO.setId(1L);
    }
    @Test
    void testCreateCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(customer);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    void testGetCustomers_withoutName() {
        when(customerRepository.findAllWithOrders()).thenReturn(List.of(customer));
        when(customerMapper.customersToCustomerDTOs(List.of(customer))).thenReturn(List.of(customerDTO));

        List<CustomerDTO> results = customerService.getCustomers(null);

        assertThat(results).isNotEmpty();
        assertThat(results).containsExactly(customerDTO);
    }

    @Test
    void testGetCustomers_withName() {
        when(customerRepository.findByNameContainingIgnoreCaseWithOrders("John")).thenReturn(List.of(customer));
        when(customerMapper.customersToCustomerDTOs(List.of(customer))).thenReturn(List.of(customerDTO));

        List<CustomerDTO> results = customerService.getCustomers("John");

        assertThat(results).isNotEmpty();
        assertThat(results).containsExactly(customerDTO);
    }

    @Test
    void testGetCustomers_withBlankName() {
        when(customerRepository.findAllWithOrders()).thenReturn(List.of(customer));
        when(customerMapper.customersToCustomerDTOs(List.of(customer))).thenReturn(List.of(customerDTO));

        List<CustomerDTO> results = customerService.getCustomers("    ");

        assertThat(results).isNotEmpty();
        assertThat(results).containsExactly(customerDTO);
    }
}