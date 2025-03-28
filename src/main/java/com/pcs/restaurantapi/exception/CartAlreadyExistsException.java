package com.pcs.restaurantapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CartAlreadyExistsException extends RuntimeException {
    public CartAlreadyExistsException(String message) {
        super(message);
    }
}
