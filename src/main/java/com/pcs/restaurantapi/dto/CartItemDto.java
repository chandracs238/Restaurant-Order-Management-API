package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.Cart;
import com.pcs.restaurantapi.model.MenuItem;

public class CartItemDto {
    private MenuItem menuItem;  // Menu item added to the cart
    private int quantity;
}
