package com.example.store.service;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderDTO createOrder(Order order) {
        log.info("Creating order for customer: {}", order.getCustomer().getId());
        return orderMapper.orderToOrderDTO(orderRepository.save(order));
    }

    public List<OrderDTO> getAllOrders() {
        log.debug("Fetching all orders.");
        List<Order> orders = orderRepository.findAllWithCustomers();

        return orderMapper.ordersToOrderDTOs(orders);
    }

    public OrderDTO getOrderById(Long id) {
        log.debug("Fetching order by id: {}", id);

        return orderRepository.findByIdWithCustomers(id)
                              .map(orderMapper::orderToOrderDTO)
                              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));
    }
}
