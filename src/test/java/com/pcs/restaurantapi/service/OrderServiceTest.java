package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.OrderDto;
import com.pcs.restaurantapi.dto.OrderItemDto;
import com.pcs.restaurantapi.dto.OrderRequest;
import com.pcs.restaurantapi.exception.NoOrderItemsException;
import com.pcs.restaurantapi.exception.OrderAccessDeniedException;
import com.pcs.restaurantapi.exception.UserNotFoundException;
import com.pcs.restaurantapi.mapper.OrderMapper;
import com.pcs.restaurantapi.model.*;
import com.pcs.restaurantapi.repository.OrderRepository;
import com.pcs.restaurantapi.repository.UserRepository;
import com.pcs.restaurantapi.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User customer;
    private User delivery;
    private Order order;
    private OrderItem orderItem;
    private MenuItem menuItem;

    private OrderDto orderDto;
    private OrderItemDto orderItemDto;

    @BeforeEach
    void setup(){
        Role customerRole = new Role("CUSTOMER");
        Role deliveryRole = new Role("DELIVERY_CREW");

        customer = new User();
        customer.setId(1L);
        customer.setUsername("pcs");
        customer.setRole(customerRole);

        delivery = new User();
        delivery.setId(2L);
        delivery.setUsername("karun");
        delivery.setRole(deliveryRole);

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setTotalAmount(BigDecimal.valueOf(800));
        order.setIdempotencyKey("@pcs_ord_01");
        order.setStatus(OrderStatus.PENDING);

        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Dosa");
        menuItem.setPrice(BigDecimal.valueOf(50));

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(16);
        orderItem.setOrder(order);
        orderItem.setPrice(BigDecimal.valueOf(800));

        order.setOrderItems(List.of(orderItem));

        orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setOrderId(orderItem.getOrder().getId());
        orderItemDto.setPrice(orderItem.getPrice());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setMenuItemId(orderItem.getMenuItem().getId());

        orderDto = new OrderDto();
        orderDto.setId(order.getId());
        orderDto.setCustomerId(order.getCustomer().getId());
        orderDto.setTotalAmount(order.getTotalAmount());
        orderDto.setIdempotencyKey(order.getIdempotencyKey());
        orderDto.setOrderItems(List.of(orderItemDto));
    }

    @Test
    void testGetAllCustomerOrders_shouldReturnAllPastOrders_whenCustomerHasOrderHistory(){
        order.setDeliveryCrew(delivery);
        order.setDeliveredAt(LocalDateTime.now().minusMonths(3));
        order.setStatus(OrderStatus.DELIVERED);
        orderDto.setDeliveryCrewId(order.getDeliveryCrew().getId());
        orderDto.setDeliveredAt(order.getDeliveredAt());
        orderDto.setStatus(order.getStatus());

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(customer));
        when(orderRepository.findAllByCustomer(customer)).thenReturn(List.of(order));
        when(orderMapper.toOrderDto(order)).thenReturn(orderDto);

        List<OrderDto> result = orderService.getAllCustomerOrders("pcs");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2L, (long) result.get(0).getDeliveryCrewId());
        assertEquals(OrderStatus.DELIVERED, result.getFirst().getStatus());
    }

    @Test
    void testGetAllCustomerOrders_shouldThrowException_whenUserNotFound(){
        when(userRepository.findByUsername("dinesh")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.getAllCustomerOrders("dinesh"));

        verify(userRepository).findByUsername("dinesh");
        verifyNoInteractions(orderRepository, orderMapper);
    }

    @Test
    void testGetAllCustomerOrders_shouldReturnEmptyList_WhenNoPastOrdersFound(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(customer));
        when(orderRepository.findAllByCustomer(customer)).thenReturn(List.of());

        List<OrderDto> result = orderService.getAllCustomerOrders("pcs");

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateCustomerOrder_shouldCreateOrderForCustomer_whenCustomerPlaceOrder(){
        List<OrderItem> orderItems = new ArrayList<>();
        List<OrderItemDto> orderItemsDto = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            orderItems.add(new OrderItem((long) i, order, menuItem, 30, BigDecimal.valueOf(50)));
            orderItemsDto.add(new OrderItemDto((long) i, order.getId(), menuItem.getId(), 30, BigDecimal.valueOf(50)));
        }
        OrderRequest orderRequest = new OrderRequest(1L, orderItems);
        order.setOrderItems(orderItems);
        orderDto.setOrderItems(orderItemsDto);
        order.setStatus(OrderStatus.PENDING);
        orderDto.setStatus(order.getStatus());

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toOrderDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.createCustomerOrder("pcs", orderRequest);

        assertEquals(10, result.getOrderItems().size());
        assertEquals(OrderStatus.PENDING, result.getStatus());
    }

    @Test
    void testCreateCustomerOrder_shouldThrowException_whenCustomerIsUnauthorized(){
        User customer2 = new User();
        customer2.setId(2L);
        customer2.setUsername("dinesh");
        OrderRequest orderRequest = new OrderRequest(1L, List.of());

        when(userRepository.findByUsername("dinesh")).thenReturn(Optional.of(customer2));

        assertThrows(OrderAccessDeniedException.class, () -> orderService.createCustomerOrder("dinesh", orderRequest));

        verifyNoInteractions(orderRepository, orderMapper);
    }

    @Test
    void testCreateCustomerOrder_shouldThrowException_whenCustomerNotFound(){
        OrderRequest orderRequest = new OrderRequest(1L, List.of());

        when(userRepository.findByUsername("Rajesh")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> orderService.createCustomerOrder("Rajesh", orderRequest));

        verifyNoInteractions(orderRepository, orderMapper);
    }

    @Test
    void testCreateCustomerOrder_shouldThrowException_whenOrderRequestIsNull(){
        OrderRequest orderRequest = new OrderRequest();

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(customer));

        assertThrows(NullPointerException.class, () -> orderService.createCustomerOrder("pcs", orderRequest));

        verifyNoInteractions(orderRepository, orderMapper);
    }

    @Test
    void testCreateCustomerOrder_shouldThrowException_whenOrderItemsIsEmpty(){
        OrderRequest orderRequest = new OrderRequest(1L, List.of());

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(customer));

        assertThrows(NoOrderItemsException.class, () -> orderService.createCustomerOrder("pcs", orderRequest));

        verifyNoInteractions(orderRepository, orderMapper);

    }

    @Test
    void testGetCustomerOrdersById_shouldReturnOrder_whenOrderIsFound(){
        order.setDeliveryCrew(delivery);
        order.setDeliveredAt(LocalDateTime.now().minusMonths(3));
        order.setStatus(OrderStatus.DELIVERED);
        orderDto.setDeliveryCrewId(order.getDeliveryCrew().getId());
        orderDto.setDeliveredAt(order.getDeliveredAt());
        orderDto.setStatus(order.getStatus());

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(customer));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.getCustomerOrderById("pcs", 1L);

        assertNotNull(result);
        assertEquals(1, result.getCustomerId());
    }

    @Test
    void testGetAllOrders_shouldReturnAllOrders(){
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toOrderDto(order)).thenReturn(orderDto);

        List<OrderDto> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test
    void testUpdateOrder_shouldUpdateOrder_whenUpdateOrder(){

    }



}
