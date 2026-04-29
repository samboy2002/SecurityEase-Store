package com.example.store.repository;

import com.example.store.entity.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"customer"})
    @Query("select o from Order o")
    Page<Order> findAllWithCustomers(Pageable pageable);

    @EntityGraph(attributePaths = {"customer"})
    @Query("select o from Order o where o.id = :id")
    Optional<Order> findByIdWithCustomers(long id);
}
