package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public CustomerDTO createCustomer(Customer customer) {
        log.info("Create a customer: {}", customer.getName());

        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    public List<CustomerDTO> getCustomers(String name) {
        log.debug("Fetch customers with name filter: {}", name);

        List<Customer> customers;
        if (name != null && !name.isBlank()) {
            customers = customerRepository.findByNameContainingIgnoreCaseWithOrders(name);
        } else {
            customers = customerRepository.findAllWithOrders();
        }

        return customerMapper.customersToCustomerDTOs(customers);
    }
}
