package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.dto.request.OrderCreateRequest;
import com.example.store.service.OrderService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "/order", description = "Order Management Operations")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get paginated orders")
    @GetMapping
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @Operation(summary = "Get a order by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Retrieval a product successfully"),
        @ApiResponse(
                responseCode = "404",
                description = "Product not found",
                content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable long id) {
        return orderService
                .getOrderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));
    }

    @Operation(summary = "Create a product")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDTO createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }
}
