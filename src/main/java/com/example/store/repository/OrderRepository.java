package com.example.store.repository;

import com.example.store.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o join fetch o.customer")
    List<Order> findAllWithCustomers();

    @Query("select o from Order o join fetch o.customer where o.id = :id")
    Optional<Order> findByIdWithCustomers(long id);
}
