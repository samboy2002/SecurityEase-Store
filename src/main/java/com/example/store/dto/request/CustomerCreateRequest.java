package com.example.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerCreateRequest {
    @NotBlank(message = "Customer name cannot be blank")
    private String name;
}
