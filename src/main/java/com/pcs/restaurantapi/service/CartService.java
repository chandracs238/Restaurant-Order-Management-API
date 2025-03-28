package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.CartItemDto;
import com.pcs.restaurantapi.model.Cart;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CartService {
    Cart createCart(Long userId);
    List<CartItemDto> getAllCartItems(Long userId, Long cartId) throws AccessDeniedException;
    CartItemDto addCartItem(Long userId, Long cartId, CartItemDto cartItemDto) throws AccessDeniedException;
    void removeCartItem(Long userId, Long cartId, Long cartItemId) throws AccessDeniedException;
    void removeCart(Long userId, Long cartId) throws AccessDeniedException;
}
