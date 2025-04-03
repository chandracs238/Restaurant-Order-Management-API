package com.pcs.restaurantapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CartItemAccessDeniedException extends RuntimeException {
    public CartItemAccessDeniedException(String message) {
        super(message);
    }
}
