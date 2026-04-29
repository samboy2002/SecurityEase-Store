package com.example.store.controller;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.dto.request.OrderCreateRequest;
import com.example.store.mapper.CustomerMapper;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
@RequiredArgsConstructor
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {
        OrderCustomerDTO customerDTO = new OrderCustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setName("John Doe");

        ProductOrderDTO product = new ProductOrderDTO();
        product.setId(1L);
        product.setDescription("Test Product");

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Test Order");
        orderDTO.setCustomer(customerDTO);
        orderDTO.setProducts(List.of(product));
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setDescription("Sample Order");
        request.setCustomerId(1L);

        when(orderService.createOrder(any(OrderCreateRequest.class))).thenReturn(orderDTO);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test Order"))
                .andExpect(jsonPath("$.customer.name").value("John Doe"));
    }

    @Test
    void testCreateOrder_withoutDescription() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setCustomerId(1L);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors[0]").value("description: Order description cannot be blank"))
                .andExpect(jsonPath("$.path").value("/order"));
    }

    @Test
    void testCreateOrder_withoutCustomerId() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setDescription("Sample Order");

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.validationErrors[0]").value("customerId: Customer ID cannot be null"))
                .andExpect(jsonPath("$.path").value("/order"));
    }

    @Test
    void testCreateOrder_withInvalidedCustomerId() throws Exception {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setDescription("Sample Order");
        request.setCustomerId(999L);

        ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        when(orderService.createOrder(any(OrderCreateRequest.class))).thenThrow(exception);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Customer not found"))
                .andExpect(jsonPath("$.path").value("/order"));
    }

    @Test
    void testGetOrder() throws Exception {
        Pageable pageable = PageRequest.of(0, 20);
        Page<OrderDTO> page = new PageImpl<>(List.of(orderDTO), pageable, 1);

        when(orderService.getAllOrders(pageable)).thenReturn(page);

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].description").value("Test Order"))
                .andExpect(jsonPath("$.content[0].customer.name").value("John Doe"))
                .andExpect(jsonPath("$.content[0].products").isArray())
                .andExpect(jsonPath("$.content[0].products[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].products[0].description").value("Test Product"));
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(orderDTO));

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test Order"))
                .andExpect(jsonPath("$.customer.name").value("John Doe"))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].id").value(1L))
                .andExpect(jsonPath("$.products[0].description").value("Test Product"));
    }

    @Test
    void testGetOrderById_withInvalidedId() throws Exception {
        when(orderService.getOrderById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/order/999")).andExpect(status().isNotFound());
    }
}
