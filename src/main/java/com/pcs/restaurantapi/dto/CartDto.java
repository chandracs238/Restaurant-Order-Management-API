package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.CartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long customerId;
    private List<CartItemDto> cartItems;
}
