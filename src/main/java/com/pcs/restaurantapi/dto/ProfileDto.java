package com.pcs.restaurantapi.dto;

import com.pcs.restaurantapi.model.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String username;
    private String email;
    private String name;
    private LocalDate dob;

    public ProfileDto(User user){
        this.email = user.getEmail();
        this.dob = user.getDob();
        this.name = user.getName();
        this.username = user.getUsername();
    }
}
