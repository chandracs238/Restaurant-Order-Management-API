package com.pcs.restaurantapi.service.impl;

import com.pcs.restaurantapi.dto.CartDto;
import com.pcs.restaurantapi.dto.CartItemDto;
import com.pcs.restaurantapi.exception.*;
import com.pcs.restaurantapi.mapper.CartMapper;
import com.pcs.restaurantapi.model.Cart;
import com.pcs.restaurantapi.model.CartItem;
import com.pcs.restaurantapi.model.MenuItem;
import com.pcs.restaurantapi.model.User;
import com.pcs.restaurantapi.repository.CartItemRepository;
import com.pcs.restaurantapi.repository.CartRepository;
import com.pcs.restaurantapi.repository.MenuItemRepository;
import com.pcs.restaurantapi.repository.UserRepository;
import com.pcs.restaurantapi.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;
    private final CartMapper cartMapper;

    @Override
    public CartDto createCart(String username) {
        User user = findUserByUsername(username);
        if (cartRepository.existsByCustomer(user)){
            throw new CartAlreadyExistsException("Cart already exists");
        }
        Cart cart = new Cart();
        cart.setCustomer(user);
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toCartDto(savedCart);
    }

    @Override
    public List<CartItemDto> getAllCartItems(String username) {
        User user = findUserByUsername(username);
        Cart cart = findCartByUsername(username);
        return cartItemRepository.findAllByCart(cart).stream()
                .map(cartMapper::toCartItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartItemDto addCartItem(String username, CartItemDto cartItemDto) {
        User user = findUserByUsername(username);
        Cart cart = findCartByUsername(username);
        MenuItem menuItem = findMenuItemById(cartItemDto.getMenuItemId());
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(cartItemDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return cartMapper.toCartItemDto(savedCartItem);
    }

    @Override
    public void removeCartItem(String username, Long cartItemId){
        User user = findUserByUsername(username);
        Cart cart = findCartByUsername(username);
        CartItem cartItem = findCartItemById(cartItemId);
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new CartItemAccessDeniedException("This cart item does not belong to your cart.");
        }
        cartItemRepository.deleteById(cartItemId);
    }


    @Override
    public void removeCart(String username) {
        Cart cart = findCartByUsername(username);
        cartRepository.delete(cart);
    }

    // ----------------- Private Helper Methods -----------------

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    private Cart findCartByUsername(String username){
        return cartRepository.findByCustomerUsername(username).orElseThrow(() -> new CartNotFoundException("Cart not found"));
    }

    private MenuItem findMenuItemById(Long menuId){
        return menuItemRepository.findById(menuId).orElseThrow(() -> new MenuItemNotFoundException("MenuItem not found"));
    }

    private CartItem findCartItemById(Long cartItemId){
        return cartItemRepository.findById(cartItemId).orElseThrow(() -> new CartItemNotFoundException("CartItem not found"));
    }
}
