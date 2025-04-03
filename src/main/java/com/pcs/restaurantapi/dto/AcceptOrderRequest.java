package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcceptOrderRequest {
    private OrderStatus status;
}
