package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.Cart;
import com.pcs.restaurantapi.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByCart(Cart cart);
}
