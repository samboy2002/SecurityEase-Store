package com.example.store.repository;

import com.example.store.entity.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @EntityGraph(attributePaths = {"orders"})
    @Query("select c from Customer c where lower(c.name) like lower(concat('%', :name, '%'))")
    Page<Customer> findByNameContainingIgnoreCaseWithOrders(@Param("name") String name, Pageable pageable);

    @EntityGraph(attributePaths = {"orders"})
    @Query("select c from Customer c")
    Page<Customer> findAllWithOrders(Pageable pageable);
}
