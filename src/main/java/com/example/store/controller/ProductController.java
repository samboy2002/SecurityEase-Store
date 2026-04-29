package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.request.ProductCreateRequest;
import com.example.store.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "/products", description = "Product Management Operations")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "Get paginated products")
    @GetMapping
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @Operation(summary = "Get product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful retrieval of a product"),
            @ApiResponse(responseCode = "404", description = "Product not found", content = @Content(
                    schema = @Schema()
            ))
    })
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    @Operation(summary = "Create a product")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return productService.createProduct(request);
    }
}
