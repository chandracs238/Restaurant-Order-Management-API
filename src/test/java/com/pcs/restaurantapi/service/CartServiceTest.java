package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.CartDto;
import com.pcs.restaurantapi.dto.CartItemDto;
import com.pcs.restaurantapi.exception.*;
import com.pcs.restaurantapi.mapper.CartMapper;
import com.pcs.restaurantapi.model.*;
import com.pcs.restaurantapi.repository.CartItemRepository;
import com.pcs.restaurantapi.repository.CartRepository;
import com.pcs.restaurantapi.repository.MenuItemRepository;
import com.pcs.restaurantapi.repository.UserRepository;
import com.pcs.restaurantapi.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Role role;
    private Cart cart;
    private CartItem cartItem;
    private MenuItem menuItem;
    private CartDto cartDto;
    private CartItemDto cartItemDto;

    @BeforeEach
    void setup(){
        role = new Role();
        role.setId(1L);
        role.setName("CUSTOMER");

        user = new User();
        user.setId(1L);
        user.setUsername("pcs");
        user.setPassword("pcs");
        user.setRole(role);

        cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(user);

        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Dosa");

        cartItem = new CartItem(1L, cart, menuItem, 4);

        cart.setCartItems(List.of(cartItem));

        cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setCartId(cartItem.getCart().getId());
        cartItemDto.setMenuItemId(cartItem.getMenuItem().getId());
        cartItemDto.setQuantity(cartItem.getQuantity());

        cartDto = new CartDto();
        cartDto.setId(cart.getId());
        cartDto.setCustomerId(cart.getCustomer().getId());
        cartDto.setCartItems(List.of(cartItemDto));
    }

    @Test
    void testCreateCart_shouldCreateCart_whenCartNotFound(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.existsByCustomer(user)).thenReturn(false);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toCartDto(cart)).thenReturn(cartDto);

        CartDto result = cartService.createCart("pcs");

        assertNotNull(result);
        assertFalse(result.getCartItems().isEmpty(), "CartItems should not be empty");
        assertEquals(1 , result.getCartItems().get(0).getMenuItemId());
    }

    @Test
    void testCreateCart_shouldNotCreateNewCart_whenCartAlreadyExists(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.existsByCustomer(user)).thenReturn(true);

        assertThrows(CartAlreadyExistsException.class, () -> cartService.createCart("pcs"));

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testCreateCart_shouldThrowException_whenUserNotFount(){
        when(userRepository.findByUsername("chandra")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cartService.createCart("chandra"));

        verifyNoInteractions(cartRepository, cartMapper);
    }

    @Test
    void testGetAllCartItems_shouldGetAllCartItems_whenCartItemsAvaliable() throws AccessDeniedException {
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.findByCustomerUsername(user.getUsername())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findAllByCart(cart)).thenReturn(List.of(cartItem));
        when(cartMapper.toCartItemDto(cartItem)).thenReturn(cartItemDto);

        List<CartItemDto> result = cartService.getAllCartItems("pcs");

        assertNotNull(result);
        assertEquals(1, result.get(0).getCartId());

        verify(cartItemRepository).findAllByCart(cart);
        verify(cartMapper).toCartItemDto(cartItem);
    }

    @Test
    void testGetAllCartItems_shouldThrowException_whenUserNotFound(){
        when(userRepository.findByUsername("Baba")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cartService.getAllCartItems("Baba"));

        verifyNoInteractions(cartMapper, cartRepository, cartItemRepository);
    }

    @Test
    void testGetAllCartItems_shouldThrowException_whenCartNotFound(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.findByCustomerUsername("pcs")).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.getAllCartItems("pcs"));

        verifyNoInteractions(cartItemRepository, cartMapper);
    }

    @Test
    void testAddCartItem_shouldAddCartItem_whenCartIsFound(){
        MenuItem idly = new MenuItem();
        idly.setId(2L);
        idly.setName("idly");
        CartItem cartItem2 = new CartItem(2L, cart, idly, 4);
        CartItemDto dto = new CartItemDto(2L, cart.getId(), idly.getId(), 4);

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.findByCustomerUsername("pcs")).thenReturn(Optional.of(cart));
        when(menuItemRepository.findById(2L)).thenReturn(Optional.of(idly));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem2);
        when(cartMapper.toCartItemDto(cartItem2)).thenReturn(dto);

        CartItemDto result = cartService.addCartItem("pcs", dto);

        assertNotNull(result);
        assertEquals(2, result.getMenuItemId());

    }

    @Test
    void testAddCartItem_shouldThrowException_whenUserNotFound(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> cartService.addCartItem("pcs", cartItemDto));

        verify(userRepository).findByUsername("pcs");
        verifyNoInteractions(cartRepository, menuItemRepository, cartItemRepository, cartMapper);
    }

    @Test
    void testAddCartItem_shouldThrowException_whenCartNotFound(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.findByCustomerUsername("pcs")).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.addCartItem("pcs", cartItemDto));

        verify(cartRepository).findByCustomerUsername("pcs");
        verify(menuItemRepository, never()).findById(1L);
    }

    @Test
    void testAddCartItem_shouldThrowException_whenMenuItemNotFound(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.findByCustomerUsername("pcs")).thenReturn(Optional.of(cart));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class, () -> cartService.addCartItem("pcs", cartItemDto));

        verify(menuItemRepository).findById(1L);
        verifyNoMoreInteractions(cartItemRepository, cartMapper);
    }

    @Test
    void testRemoveCartItem_shouldRemoveCartItemFromCart_whenCartItemFoundInCart(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.findByCustomerUsername("pcs")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).deleteById(1L);

        cartService.removeCartItem("pcs", 1L);

        verify(cartItemRepository).deleteById(1L);
    }

    @Test
    void testRemoveCartItem_shouldThrowException_whenCartItemNotFoundInCart(){
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(cartRepository.findByCustomerUsername("pcs")).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(CartItemNotFoundException.class, () -> cartService.removeCartItem("pcs", 20L));

        verify(cartItemRepository, never()).deleteById(20L);
    }

    @Test
    void testRemoveCart_ShouldRemoveCart_whenCartFoundForUser(){
        when(cartRepository.findByCustomerUsername("pcs")).thenReturn(Optional.of(cart));
        doNothing().when(cartRepository).delete(any(Cart.class));

        cartService.removeCart("pcs");

        verify(cartRepository).delete(cart);
    }

    @Test
    void testRemoveCart_shouldThrowException_whenCartNotFound(){
        when(cartRepository.findByCustomerUsername("pcs")).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.removeCart("pcs"));

        verify(cartRepository, never()).delete(cart);
    }

}
