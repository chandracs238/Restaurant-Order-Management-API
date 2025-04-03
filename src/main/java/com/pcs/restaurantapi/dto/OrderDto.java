package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private Long customerId;
    private List<OrderItemDto> orderItems;
    private OrderStatus status; // PENDING, CONFIRMED, OUT_FOR_DELIVERY, DELIVERED, CANCELED
    private Long deliveryCrewId;
    private BigDecimal totalAmount;
    private String idempotencyKey;
    private LocalDateTime deliveredAt;
}
