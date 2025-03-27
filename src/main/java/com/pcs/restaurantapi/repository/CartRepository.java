package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
