package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.Cart;
import com.pcs.restaurantapi.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private Long cartId;
    private Long menuItemId;  // Menu item added to the cart
    private int quantity;
}
