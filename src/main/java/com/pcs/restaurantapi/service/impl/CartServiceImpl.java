package com.pcs.restaurantapi.service.impl;

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

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    public Cart createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (cartRepository.existsByCustomer(user)){
            throw new CartAlreadyExistsException("Cart already exists");
        }
        Cart cart = new Cart();
        cart.setCustomer(user);
        return cartRepository.save(cart);
    }

    @Override
    public List<CartItemDto> getAllCartItems(Long userId, Long cartId) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        if (!cart.getCustomer().getId().equals(user.getId())){
            throw new AccessDeniedException("You do not own this cart");
        }
        return cartItemRepository.findAllByCart(cart).stream()
                .map(CartMapper::toCartItemDto)
                .toList();
    }

    @Override
    public CartItemDto addCartItem(Long userId, Long cartId, CartItemDto cartItemDto) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        if (!cart.getCustomer().getId().equals(user.getId())){
            throw new AccessDeniedException("You do not own this cart");
        }
        MenuItem menuItem = menuItemRepository.findById(cartItemDto.getMenuItemId())
                .orElseThrow(() -> new MenuItemNotFoundException("MenuItem not found"));
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(cartItemDto.getQuantity());
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return CartMapper.toCartItemDto(savedCartItem);
    }

    @Override
    public void removeCartItem(Long userId, Long cartId, Long cartItemId) throws AccessDeniedException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        if (!cart.getCustomer().getId().equals(user.getId())){
            throw new AccessDeniedException("You do not own this cart");
        }
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("CartItem not found"));
        if (!cartItem.getCart().getId().equals(cartId)) {
            throw new AccessDeniedException("This cart item does not belong to your cart.");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void removeCart(Long userId, Long cartId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        cartRepository.delete(cart);
    }
}
