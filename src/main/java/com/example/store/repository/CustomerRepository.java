package com.example.store.repository;

import com.example.store.entity.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "select distinct c from Customer c left join fetch c.orders where lower(c.name) like lower(concat('%', :name, '%'))",
            countQuery = "select count(c) from Customer c where lower(c.name) like lower(concat('%', :name, '%'))")
    Page<Customer> findByNameContainingIgnoreCaseWithOrders(@Param("name") String name, Pageable pageable);

    @Query(value = "select distinct c from Customer c left join fetch c.orders",
            countQuery = "select count(c) from Customer c")
    Page<Customer> findAllWithOrders(Pageable pageable);
}
