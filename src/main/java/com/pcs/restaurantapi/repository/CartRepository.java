package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.Cart;
import com.pcs.restaurantapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    boolean existsByCustomer(User customer);
}
