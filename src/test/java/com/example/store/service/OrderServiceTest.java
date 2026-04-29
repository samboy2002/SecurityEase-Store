package com.example.store.service;

import com.example.store.dto.OrderCustomerDTO;
import com.example.store.dto.OrderDTO;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    }

    @Test
    void testCreateOrder() {
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(order);

        assertThat(result).isEqualTo(orderDTO);
    }

    @Test
    void testGetAllOrders() {
        when(orderRepository.findAllWithCustomers()).thenReturn(List.of(order));
        when(orderMapper.ordersToOrderDTOs(List.of(order))).thenReturn(List.of(orderDTO));

        List<OrderDTO> results = orderService.getAllOrders();

        assertThat(results).isNotEmpty();
        assertThat(results).containsExactly(orderDTO);
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