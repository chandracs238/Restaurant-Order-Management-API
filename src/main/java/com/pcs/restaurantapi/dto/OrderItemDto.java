package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.MenuItem;
import com.pcs.restaurantapi.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private Long orderId;
    private Long menuItemId;
    private int quantity;
    private BigDecimal price;
}
