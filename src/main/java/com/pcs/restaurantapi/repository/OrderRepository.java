package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.Order;
import com.pcs.restaurantapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(User customer);
    List<Order> findAllByDeliveryCrew(User deliveryCrew);
}
