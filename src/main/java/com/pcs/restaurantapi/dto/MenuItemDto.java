package com.pcs.restaurantapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDto {
    private String name;
    private BigDecimal price;
    private String category;
    private String description;
}
