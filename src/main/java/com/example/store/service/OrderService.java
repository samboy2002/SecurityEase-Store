package com.example.store.service;

import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        log.debug("Fetching all orders with pagination: {}", pageable);

        if (pageable.isUnpaged()) {
            pageable = PageRequest.of(0, 20);
        }

        Page<Order> orders = orderRepository.findAllWithCustomers(pageable);

        return orders.map(orderMapper::orderToOrderDTO);
    }

    public Optional<OrderDTO> getOrderById(Long id) {
        log.debug("Fetching order by id: {}", id);

        return orderRepository.findByIdWithCustomers(id)
                              .map(orderMapper::orderToOrderDTO);
    }
}
