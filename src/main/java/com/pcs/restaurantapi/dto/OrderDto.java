package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.OrderItem;
import com.pcs.restaurantapi.model.OrderStatus;
import com.pcs.restaurantapi.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private User customer;
    private List<OrderItem> orderItems;
    private OrderStatus status; // PENDING, CONFIRMED, OUT_FOR_DELIVERY, DELIVERED, CANCELED
    private User deliveryCrew;
    private BigDecimal totalAmount;
}
