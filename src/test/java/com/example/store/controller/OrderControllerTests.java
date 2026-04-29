package com.example.store.controller;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.mapper.CustomerMapper;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
        when(orderService.createOrder(any())).thenReturn(orderDTO);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test Order"))
                .andExpect(jsonPath("$.customer.name").value("John Doe"));
    }

    @Test
    void testGetOrder() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(orderDTO));

        mockMvc.perform(get("/order"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].description").value("Test Order"))
               .andExpect(jsonPath("$[0].customer.name").value("John Doe"))
               .andExpect(jsonPath("$[0].products").isArray())
               .andExpect(jsonPath("$[0].products[0].id").value(1L))
               .andExpect(jsonPath("$[0].products[0].description").value("Test Product"));
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

        mockMvc.perform(get("/order/999"))
               .andExpect(status().isNotFound());
    }
}
