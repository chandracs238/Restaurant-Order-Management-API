package com.pcs.restaurantapi.mapper;

import com.pcs.restaurantapi.dto.OrderDto;
import com.pcs.restaurantapi.dto.OrderItemDto;
import com.pcs.restaurantapi.model.MenuItem;
import com.pcs.restaurantapi.model.Order;
import com.pcs.restaurantapi.model.OrderItem;
import com.pcs.restaurantapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public static OrderDto toOrderDto(Order order){
        if (order == null){
            return null;
        }
        return new OrderDto(
                order.getId(),
                order.getCustomer().getId(),
                order.getStatus(),
                order.getDeliveryCrew().getId(),
                order.getTotalAmount(),
                order.getIdempotencyKey(),
                order.getDeliveredAt()
        );
    }

    public static OrderItemDto toOrderItemDto(OrderItem orderItem){
        if (orderItem == null){
            return null;
        }
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getOrder().getId(),
                orderItem.getMenuItem().getId(),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }

    public static Order toOrderEntity(OrderDto orderDto, User customer, User deliveryCrew){
        if (orderDto == null){
            return null;
        }
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setCustomer(customer);
        order.setStatus(orderDto.getStatus());
        order.setDeliveryCrew(deliveryCrew);
        order.setTotalAmount(orderDto.getTotalAmount());
        order.setIdempotencyKey(orderDto.getIdempotencyKey());
        order.setDeliveredAt(orderDto.getDeliveredAt());
        return order;
    }

    public static OrderItem toOrderItemDto(OrderItemDto orderItemDto, MenuItem menuItem, Order order){
        if (orderItemDto == null){
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDto.getId());
        orderItem.setMenuItem(menuItem);
        orderItem.setOrder(order);
        orderItem.setPrice(orderItemDto.getPrice());
        orderItem.setQuantity(orderItemDto.getQuantity());
        return orderItem;
    }
}
