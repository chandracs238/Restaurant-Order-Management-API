package com.pcs.restaurantapi.controller;

import com.pcs.restaurantapi.dto.ProfileDto;
import com.pcs.restaurantapi.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileDto> getProfile(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        return ResponseEntity.ok(userService.getProfile(username));
    }

    @PutMapping("/profile")
    public ResponseEntity<ProfileDto> updateProfile(@AuthenticationPrincipal UserDetails userDetails ,@Valid @RequestBody ProfileDto profileDto){
        String username = userDetails.getUsername();
        return ResponseEntity.ok(userService.updateProfile(username, profileDto));
    }
}
