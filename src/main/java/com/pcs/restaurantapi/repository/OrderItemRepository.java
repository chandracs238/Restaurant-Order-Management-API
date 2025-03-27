package com.pcs.restaurantapi.repository;


import com.pcs.restaurantapi.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
