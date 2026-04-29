package com.example.store.service;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.request.OrderCreateRequest;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderDTO orderDTO;

    private Customer customer;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(2L);
        customer.setName("Jane Doe");

        order = new Order();
        order.setId(1L);
        order.setDescription("Sample Order");
        order.setCustomer(customer);

        OrderCustomerDTO customerDTO = new OrderCustomerDTO();
        customerDTO.setId(2L);
        customerDTO.setName("Jane Doe");

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setDescription("Sample Order");
        orderDTO.setCustomer(customerDTO);

        pageable = PageRequest.of(0, 20);
    }

    @Test
    void testCreateOrder() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setDescription("Sample Order");
        request.setCustomerId(2L);

        Order savedOrder = new Order();
        savedOrder.setDescription("Sample Order");
        savedOrder.setId(1L);
        savedOrder.setCustomer(customer);

        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.orderToOrderDTO(savedOrder)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(request);

        assertThat(result).isEqualTo(orderDTO);
    }

    @Test
    void testCreateOrder_withInvalidedCustomerId() {
        OrderCreateRequest request = new OrderCreateRequest();
        request.setDescription("Sample Order");
        request.setCustomerId(999L);

        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void testGetAllOrders() {
        Page<Order> page = new PageImpl<>(List.of(order), pageable, 1);

        when(orderRepository.findAllWithCustomers(pageable)).thenReturn(page);
        when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        Page<OrderDTO> results = orderService.getAllOrders(pageable);

        assertThat(results).isNotEmpty();
        assertThat(results.getContent()).containsExactly(orderDTO);
    }

    @Test
    void testGetAllOrders_unpaged() {
        Page<Order> page = new PageImpl<>(List.of(order), pageable, 1);

        when(orderRepository.findAllWithCustomers(pageable)).thenReturn(page);
        when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        Page<OrderDTO> results = orderService.getAllOrders(Pageable.unpaged());

        assertThat(results).isNotEmpty();
        assertThat(results.getContent()).containsExactly(orderDTO);
    }

    @Test
    void testGetOrderById() {
        when(orderRepository.findByIdWithCustomers(1L)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        Optional<OrderDTO> result = orderService.getOrderById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(orderDTO);
    }

    @Test
    void testGetOrderById_withInvalidedId() {
        when(orderRepository.findByIdWithCustomers(999L)).thenReturn(Optional.empty());

        Optional<OrderDTO> result = orderService.getOrderById(999L);
        assertThat(result).isNotPresent();
    }
}
