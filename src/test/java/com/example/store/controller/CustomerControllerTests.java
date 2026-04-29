package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.request.CustomerCreateRequest;
import com.example.store.entity.Customer;
import com.example.store.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
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
    void testCreateCustomer() throws Exception {
        CustomerCreateRequest createRequest = new CustomerCreateRequest();
        createRequest.setName("John Doe");

        when(customerService.createCustomer(createRequest)).thenReturn(customerDTO);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testCreateCustomer_withBlankName() throws Exception {
        CustomerCreateRequest createRequest = new CustomerCreateRequest();

        when(customerService.createCustomer(createRequest)).thenReturn(customerDTO);

        mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors[0]").value("name: Customer name cannot be blank"))
                .andExpect(jsonPath("$.path").value("/customer"));
    }

    @Test
    void testGetAllCustomers() throws Exception {
        Page<CustomerDTO> page = new PageImpl<>(List.of(customerDTO), pageable, 1);

        when(customerService.getCustomers(null, pageable)).thenReturn(page);

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name").value("John Doe"));
        ;
    }

    @Test
    void testSearchCustomersByName() throws Exception {
        Page<CustomerDTO> page = new PageImpl<>(List.of(customerDTO), pageable, 1);

        when(customerService.getCustomers("John", pageable)).thenReturn(page);

        mockMvc.perform(get("/customer?name=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"));
    }

    @Test
    void testSearchCustomersByName_withBlankString() throws Exception {
        Page<CustomerDTO> page = new PageImpl<>(List.of(customerDTO), pageable, 1);

        when(customerService.getCustomers("    ", pageable)).thenReturn(page);

        mockMvc.perform(get("/customer?name=    "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..name").value("John Doe"));
    }

    @Test
    void testSearchCustomersByName_noResults() throws Exception {
        Page<CustomerDTO> page = new PageImpl<>(List.of(), pageable, 0);

        when(customerService.getCustomers("Alice", pageable)).thenReturn(page);

        mockMvc.perform(get("/customer?name=Alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
