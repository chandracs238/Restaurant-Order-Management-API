package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.AcceptOrderRequest;
import com.pcs.restaurantapi.dto.OrderDto;
import com.pcs.restaurantapi.dto.OrderRequest;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllCustomerOrders(String username);
    OrderDto createCustomerOrder(String username, OrderRequest orderRequest);
    OrderDto getCustomerOrderById(String username, Long orderId);
    List<OrderDto> getAllOrders();
    OrderDto acceptOrderRequest(String username, Long orderId, AcceptOrderRequest acceptOrderRequest);
    OrderDto updateOrder(String username, Long orderId, OrderDto orderDto);
    void deleteOrder(String username, Long orderId);
    List<OrderDto> getAllAssignedOrders(String username);
    OrderDto updateOrderStatus(String username, OrderDto orderDto);
}
