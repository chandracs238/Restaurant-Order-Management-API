package com.pcs.restaurantapi.controller;

import com.pcs.restaurantapi.dto.CartItemDto;
import com.pcs.restaurantapi.service.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems(@AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        return ResponseEntity.ok(cartService.getAllCartItems(username));
    }

    @PostMapping
    public ResponseEntity<?> addCartItemToCart(String username, @RequestBody CartItemDto cartItemDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addCartItem(username, cartItemDto));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId, String username) {
        cartService.removeCartItem(username, cartItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted CartItem");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCart(String username){
        cartService.removeCart(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted Cart");
    }
}
