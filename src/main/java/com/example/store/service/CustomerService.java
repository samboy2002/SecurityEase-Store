package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.request.CustomerCreateRequest;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerDTO createCustomer(CustomerCreateRequest request) {
        log.info("Creating a customer: {}", request.getName());

        Customer customer = new Customer();
        customer.setName(request.getName());
        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.customerToCustomerDTO(savedCustomer);
    }

    public Page<CustomerDTO> getCustomers(String name, Pageable pageable) {
        log.debug("Fetching customers with name filter: {}, pagination: {}", name, pageable);

        if (pageable.isUnpaged()) {
            pageable = PageRequest.of(0, 20);
        }

        Page<Customer> customers;
        if (name != null && !name.isBlank()) {
            customers = customerRepository.findByNameContainingIgnoreCaseWithOrders(name, pageable);
        } else {
            customers = customerRepository.findAllWithOrders(pageable);
        }

        return customers.map(customerMapper::customerToCustomerDTO);
    }
}
