package com.pcs.restaurantapi.mapper;

import com.pcs.restaurantapi.dto.CartDto;
import com.pcs.restaurantapi.dto.CartItemDto;
import com.pcs.restaurantapi.model.Cart;
import com.pcs.restaurantapi.model.CartItem;
import com.pcs.restaurantapi.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartMapper {

    public static CartDto toCartDto(Cart cart){
        if (cart == null){
            return null;
        }
        List<CartItemDto> cartItems = cart.getCartItems().stream()
                .map(CartMapper::toCartItemDto)
                .toList();
        return new CartDto(
                cart.getId(),
                cart.getCustomer().getId(),
                cartItems
        );
    }

    public static CartItemDto toCartItemDto(CartItem cartItem){
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getCart().getId(),
                cartItem.getMenuItem().getId(),
                cartItem.getQuantity()
        );
    }

    public static Cart toCartEntity(CartDto cartDto, User customer){
        Cart cart = new Cart();
        cart.setId(cartDto.getId());
        cart.setCustomer(customer);
        return cart;
    }

}
