package com.example.store.repository;

import com.example.store.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select distinct c from Customer c left join fetch c.orders where lower(c.name) like lower(concat('%', :name, '%'))")
    List<Customer> findByNameContainingIgnoreCaseWithOrders(String name);

    @Query("select distinct c from Customer c left join fetch c.orders")
    List<Customer> findAllWithOrders();
}
