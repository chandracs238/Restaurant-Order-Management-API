package com.pcs.restaurantapi.controller;

import com.pcs.restaurantapi.dto.CartItemDto;
import com.pcs.restaurantapi.service.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartItems(@RequestParam Long userId, @RequestParam Long cartId) throws AccessDeniedException {
        return ResponseEntity.ok(cartService.getAllCartItems(userId, cartId));
    }

    @PostMapping
    public ResponseEntity<?> createCart(@RequestParam Long userId, @RequestParam Long cartId, @RequestBody CartItemDto cartItemDto) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cartService.addCartItem(userId, cartId, cartItemDto));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId, @RequestParam Long cartId, @RequestParam Long userId) throws AccessDeniedException {
        cartService.removeCartItem(userId, cartId, cartItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted CartItem");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCart(@RequestParam Long userId, @RequestParam Long cartId) throws AccessDeniedException {
        cartService.removeCart(userId, cartId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Deleted Cart");
    }
}
