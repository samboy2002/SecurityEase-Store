package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.request.CustomerCreateRequest;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setName("John Doe");
        customer.setId(1L);

        customerDTO = new CustomerDTO();
        customerDTO.setName("John Doe");
        customerDTO.setId(1L);

        pageable = PageRequest.of(0, 20);
    }

    @Test
    void testCreateCustomer() {
        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setName("Jhon Doe");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(request);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
    }

    @Test
    void testGetCustomers_withoutName() {
        Page<Customer> page = new PageImpl<>(List.of(customer), pageable, 1);
        when(customerRepository.findAllWithOrders(pageable)).thenReturn(page);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDTO);

        Page<CustomerDTO> results = customerService.getCustomers(null, pageable);

        assertThat(results).isNotEmpty();
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent()).containsExactly(customerDTO);
    }

    @Test
    void testGetCustomers_withoutNameAndUnpaged() {
        Page<Customer> page = new PageImpl<>(List.of(customer), pageable, 1);

        when(customerRepository.findAllWithOrders(pageable)).thenReturn(page);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDTO);

        Page<CustomerDTO> results = customerService.getCustomers(null, Pageable.unpaged());

        assertThat(results).isNotEmpty();
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent()).containsExactly(customerDTO);
    }

    @Test
    void testGetCustomers_withName() {
        Page<Customer> page = new PageImpl<>(List.of(customer), pageable, 1);

        when(customerRepository.findByNameContainingIgnoreCaseWithOrders("John", pageable))
                .thenReturn(page);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDTO);

        Page<CustomerDTO> results = customerService.getCustomers("John", pageable);

        assertThat(results).isNotEmpty();
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent()).containsExactly(customerDTO);
    }

    @Test
    void testGetCustomers_withBlankName() {
        Page<Customer> page = new PageImpl<>(List.of(customer), pageable, 0);

        when(customerRepository.findAllWithOrders(pageable)).thenReturn(page);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDTO);

        Page<CustomerDTO> results = customerService.getCustomers("    ", pageable);

        assertThat(results).isNotEmpty();
        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent()).containsExactly(customerDTO);
    }
}
