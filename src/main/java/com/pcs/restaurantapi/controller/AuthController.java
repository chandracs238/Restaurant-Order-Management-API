package com.pcs.restaurantapi.controller;

import com.pcs.restaurantapi.dto.AuthResponse;
import com.pcs.restaurantapi.dto.UserDto;
import com.pcs.restaurantapi.security.JwtUtil;
import com.pcs.restaurantapi.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtUtil jwtUtil, UserDetailsService userDetailsService, UserServiceImpl userService, AuthenticationManager authenticationManager){
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserDto userDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        SecurityContextHolder.getContext().getAuthentication().getAuthorities().forEach(System.out::println);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.createUser(userDto));
    }
}
