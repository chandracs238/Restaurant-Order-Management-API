package com.pcs.restaurantapi.service.impl;

import com.pcs.restaurantapi.dto.OrderDto;
import com.pcs.restaurantapi.exception.OrderNotFoundException;
import com.pcs.restaurantapi.exception.UserNotFoundException;
import com.pcs.restaurantapi.mapper.OrderMapper;
import com.pcs.restaurantapi.model.Order;
import com.pcs.restaurantapi.model.User;
import com.pcs.restaurantapi.repository.OrderRepository;
import com.pcs.restaurantapi.repository.UserRepository;
import com.pcs.restaurantapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;


    @Override
    public List<OrderDto> getAllCustomerOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return orderRepository.findAllByCustomer(user).stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto createCustomerOrder(Long userId, OrderDto orderDto) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (!orderDto.getCustomerId().equals(user.getId())){
            throw new AccessDeniedException("Incorrect Order");
        }
        Order order = OrderMapper.toOrderEntity(orderDto, user, null);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toOrderDto(savedOrder);
    }

    @Override
    public OrderDto getCustomerOrderById(Long userId, Long orderId) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getCustomer().getId().equals(user.getId())){
            throw new AccessDeniedException("Unauthorized access");
        }
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderMapper::toOrderDto).toList();
    }

    @Override
    public OrderDto updateOrder(Long userId, Long orderId, OrderDto orderDto) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getCustomer().getId().equals(user.getId())){
            throw new AccessDeniedException("Unauthorized access");
        }
        User deliveryCrew = userRepository.findById(orderDto.getDeliveryCrewId())
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
        order.setDeliveryCrew(deliveryCrew);
        Order updatedOrder = orderRepository.save(order);
        return OrderMapper.toOrderDto(order);
    }

    @Override
    public void deleteOrder(Long userId, Long orderId) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getCustomer().getId().equals(user.getId())){
            throw new AccessDeniedException("Unauthorized access");
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderDto> getAllAssignedOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return orderRepository.findAllByDeliveryCrew(user).stream()
                .map(OrderMapper::toOrderDto).toList();
    }

    @Override
    public OrderDto updateOrderStatus(OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(orderDto.getStatus());
        return OrderMapper.toOrderDto(order);
    }
}
