package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.User;
import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
}


