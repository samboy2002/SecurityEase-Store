package com.example.store.exception;

import com.example.store.controller.CustomerController;
import com.example.store.service.CustomerService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void getAllCustomers_whenUnexpectedExceptionOccur_return500() throws Exception {
        when(customerService.getCustomers(any(), any())).thenThrow(new RuntimeException("Database connection lost"));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("500 INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/customer"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getCustomerById_whenNotFound_return404() throws Exception {
        when(customerService.getCustomers(any(), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        // Act & Assert
        mockMvc.perform(get("/customer"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("404 NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Customer not found"))
                .andExpect(jsonPath("$.path").value("/customer"));
    }
}
