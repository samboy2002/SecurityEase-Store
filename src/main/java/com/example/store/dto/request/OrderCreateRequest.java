package com.example.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class OrderCreateRequest {
    @NotBlank(message = "Order description cannot be blank")
    private String description;

    @NotNull(message = "Customer ID cannot be null") private Long customerId;
}
