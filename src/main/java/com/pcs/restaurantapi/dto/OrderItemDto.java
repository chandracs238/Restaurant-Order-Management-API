package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.MenuItem;
import com.pcs.restaurantapi.model.Order;

import java.math.BigDecimal;

public class OrderItemDto {
    private Order order;
    private MenuItem menuItem;
    private int quantity;
    private BigDecimal price;
}
