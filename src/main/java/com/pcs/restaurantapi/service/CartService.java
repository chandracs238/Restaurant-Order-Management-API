package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.CartDto;
import com.pcs.restaurantapi.dto.CartItemDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CartService {
    CartDto createCart(String username);
    List<CartItemDto> getAllCartItems(String username);
    CartItemDto addCartItem(String username, CartItemDto cartItemDto);
    void removeCartItem(String username, Long cartItemId);
    void removeCart(String username);
}
