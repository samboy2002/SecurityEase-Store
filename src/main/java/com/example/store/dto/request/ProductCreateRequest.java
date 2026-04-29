package com.example.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductCreateRequest {
    @NotBlank(message = "Product description cannot be blank")
    private String description;
}
