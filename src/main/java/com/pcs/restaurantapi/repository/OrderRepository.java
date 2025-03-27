package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
