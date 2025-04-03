package com.pcs.restaurantapi.controller;

import com.pcs.restaurantapi.dto.OrderDto;
import com.pcs.restaurantapi.dto.OrderRequest;
import com.pcs.restaurantapi.service.impl.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderService;

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping
    public ResponseEntity<?> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<?> createOrder(String username, @RequestBody OrderRequest orderRequest){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createCustomerOrder(username, orderRequest));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId, String username){
        return ResponseEntity.ok(orderService.getCustomerOrderById(username,orderId));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrderById(@PathVariable Long orderId, @RequestParam Long userId, @RequestBody OrderDto orderDto) throws AccessDeniedException {
        return ResponseEntity.ok(orderService.updateOrder(userId, orderId, orderDto));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteO   dfgffdgfdgfdgdfggdgfdgfdgdffgdgfdgfdgfgfgfdgfdfgfgdfgfffgdfgdfggfdfdgfdgfdfdgrderById(@RequestParam Long userId, @PathVariable Long orderId) throws AccessDeniedException {
        orderService.deleteOrder(userId, orderId);
        return ResponseEntity.ok("deleted successfully");
    }

    @PreAuthorize("hasRole('DELIVERY_CREW')")
    @GetMapping("/assigned")
    public ResponseEntity<?> getOrdersForDeliveryCrew(@RequestParam Long userId){
        return ResponseEntity.ok(orderService.getAllAssignedOrders(userId));
    }

    @PreAuthorize("hasRole('DELIVERY_CREW')")
    @PatchMapping("/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderDto orderDto){
        return ResponseEntity.ok(orderService.updateOrderStatus(orderDto));
    }

}
