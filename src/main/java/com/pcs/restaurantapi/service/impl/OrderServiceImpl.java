package com.pcs.restaurantapi.service.impl;

import com.pcs.restaurantapi.dto.AcceptOrderRequest;
import com.pcs.restaurantapi.dto.OrderDto;
import com.pcs.restaurantapi.dto.OrderRequest;
import com.pcs.restaurantapi.exception.NoOrderItemsException;
import com.pcs.restaurantapi.exception.OrderAccessDeniedException;
import com.pcs.restaurantapi.exception.OrderNotFoundException;
import com.pcs.restaurantapi.exception.UserNotFoundException;
import com.pcs.restaurantapi.mapper.OrderMapper;
import com.pcs.restaurantapi.model.Order;
import com.pcs.restaurantapi.model.OrderStatus;
import com.pcs.restaurantapi.model.User;
import com.pcs.restaurantapi.repository.OrderRepository;
import com.pcs.restaurantapi.repository.UserRepository;
import com.pcs.restaurantapi.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private OrderMapper orderMapper;


    @Override
    public List<OrderDto> getAllCustomerOrders(String username) {
        User user = findUserByUsername(username);
        return orderRepository.findAllByCustomer(user).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto createCustomerOrder(String username, OrderRequest orderRequest) {
        User user = findUserByUsername(username);
        if (!orderRequest.getCustomerId().equals(user.getId())){
            throw new OrderAccessDeniedException("Incorrect Order! Unknown user placed order");
        }
        if (orderRequest.getOrderItems().isEmpty()){
            throw new NoOrderItemsException("No items to place Order!");
        }
        Order order = new Order();
        order.setCustomer(user);
        order.setOrderItems(orderRequest.getOrderItems());
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderDto(savedOrder);
    }

    @Override
    public OrderDto getCustomerOrderById(String username, Long orderId){
        User user = findUserByUsername(username);
        Order order = findOrderById(orderId);
        if (!order.getCustomer().getId().equals(user.getId())){
            throw new OrderAccessDeniedException("Unauthorized access");
        }
        return orderMapper.toOrderDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toOrderDto).toList();
    }

    @Override
    public OrderDto acceptOrderRequest(String username, Long orderId, AcceptOrderRequest acceptOrderRequest){
        User user = findUserByUsername(username);
        Order order = findOrderById(orderId);
        order.setStatus(acceptOrderRequest.getStatus());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toOrderDto(savedOrder);
    }

    @Override
    public OrderDto updateOrder(String username, Long orderId, OrderDto orderDto) {
        User user = findUserByUsername(username);
        Order order = findOrderById(orderId);
        User deliveryCrew = userRepository.findById(orderDto.getDeliveryCrewId())
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
        order.setDeliveryCrew(deliveryCrew);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }

    @Override
    public void deleteOrder(String username, Long orderId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!order.getCustomer().getId().equals(user.getId())){
            throw new OrderAccessDeniedException("Unauthorized access");
        }
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderDto> getAllAssignedOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return orderRepository.findAllByDeliveryCrew(user).stream()
                .map(orderMapper::toOrderDto).toList();
    }

    @Override
    public OrderDto updateOrderStatus(String username, OrderDto orderDto) {
        Order order = orderRepository.findById(orderDto.getId()).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(orderDto.getStatus());
        return orderMapper.toOrderDto(order);
    }

    private User findUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Order findOrderById(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
    }
}
