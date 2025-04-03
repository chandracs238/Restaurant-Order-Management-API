package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Cart cart;
    private MenuItem menuItem;

    @BeforeEach
    void setup() {
        // Ensure role exists
        Role role = entityManager.getEntityManager()
                .createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", "CUSTOMER")
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("CUSTOMER role must be pre-populated"));

        // Create user
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRole(role);
        entityManager.persist(user);

        // Create cart
        cart = new Cart();
        cart.setCustomer(user);
        entityManager.persist(cart);

        // Create menu item
        menuItem = new MenuItem();
        menuItem.setName("Dosa");
        menuItem.setPrice(BigDecimal.valueOf(40));
        menuItem.setCategory("Tiffin");
        menuItem.setAvailable(true);
        entityManager.persist(menuItem);

        entityManager.flush(); // Ensures IDs are generated before testing
    }

    @Test
    void testFindAllByCart_shouldReturnCartItemList_whenCartItemsAvailable() {
        // Arrange
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMenuItem(menuItem);
        cartItem.setQuantity(4);
        entityManager.persist(cartItem);
        entityManager.flush();

        // Act
        List<CartItem> foundCartItems = cartItemRepository.findAllByCart(cart);

        // Assert
        assertEquals(1, foundCartItems.size(), "Expected one cart item");
        CartItem foundItem = foundCartItems.get(0);
        assertEquals("Dosa", foundItem.getMenuItem().getName(), "Item name mismatch");
        assertEquals(4, foundItem.getQuantity(), "Quantity mismatch");
    }

    @Test
    void testFindAllByCart_shouldReturnEmptyList_whenNoCartItems() {
        // Act
        List<CartItem> foundCartItems = cartItemRepository.findAllByCart(cart);

        // Assert
        assertTrue(foundCartItems.isEmpty(), "Expected empty cart but found items");
    }
}
