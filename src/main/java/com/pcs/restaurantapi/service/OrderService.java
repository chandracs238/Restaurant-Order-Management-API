package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.OrderDto;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllCustomerOrders(Long userId);
    OrderDto createCustomerOrder(Long userId, OrderDto orderDto) throws AccessDeniedException;
    OrderDto getCustomerOrderById(Long userId, Long orderId) throws AccessDeniedException;
    List<OrderDto> getAllOrders();
    OrderDto updateOrder(Long userId, Long orderId, OrderDto orderDto) throws AccessDeniedException;
    void deleteOrder(Long userId, Long orderId) throws AccessDeniedException;
    List<OrderDto> getAllAssignedOrders(Long userId);
    OrderDto updateOrderStatus(OrderDto orderDto);
}
