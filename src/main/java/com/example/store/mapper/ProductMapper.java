package com.example.store.mapper;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.ProductOrderDTO;
import com.example.store.entity.Order;
import com.example.store.entity.Product;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", imports = Order.class)
public interface ProductMapper {
    @Mapping(
            target = "orderIds",
            expression = "java(product.getOrders().stream().map(com.example.store.entity.Order::getId).toList())")
    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productsToProductDTOs(List<Product> products);

    ProductOrderDTO productToProductOrderDTO(Product product);

    List<ProductOrderDTO> productsToProductOrderDTOs(List<Product> products);
}
