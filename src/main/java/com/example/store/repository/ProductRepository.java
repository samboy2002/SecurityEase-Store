package com.example.store.repository;

import com.example.store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p left join fetch p.orders")
    List<Product> findAllWithOrders();

    @Query("select p from Product p left join fetch p.orders where p.id = :id")
    Optional<Product> findByIdWithOrders(Long id);
}
