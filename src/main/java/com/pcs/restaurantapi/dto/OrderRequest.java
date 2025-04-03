package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.OrderItem;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {
    @NotNull
    private Long customerId;
    @NotNull
    private List<OrderItem> orderItems;
}
