package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.request.CustomerCreateRequest;
import com.example.store.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Tag(name = "/customer", description = "Customer Management Operations")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Get customers")
    @GetMapping
    public Page<CustomerDTO> getAllCustomers(@RequestParam(required = false) String name, Pageable pageable) {
        return customerService.getCustomers(name, pageable);
    }

    @Operation(summary = "Create a customer")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@Valid @RequestBody CustomerCreateRequest request) {
        return customerService.createCustomer(request);
    }
}
