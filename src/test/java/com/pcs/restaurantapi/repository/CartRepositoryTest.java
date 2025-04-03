package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.exception.RoleNotFoundException;
import com.pcs.restaurantapi.model.Cart;
import com.pcs.restaurantapi.model.Role;
import com.pcs.restaurantapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Cart cart;
    private User user;
    private User user2;
    private Role role;

    @BeforeEach
    void setUp(){
        role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRole(role);
        userRepository.save(user);

        cart = new Cart();
        cart.setCustomer(user);
        cartRepository.save(cart);

        user2 = new User();
        user2.setUsername("anotherUser");
        user2.setPassword("password");
        user2.setRole(role);
        userRepository.save(user2);
        // not creating any cart for user2
    }

    @Test
    void testExistsByCustomer_shouldReturnTrue_whenCartExists(){
        boolean result = cartRepository.existsByCustomer(user);
        assertTrue(result);
    }

    @Test
    void testExistsByCustomer_shouldReturnFalse_WhenCartDoesNotExists(){
        boolean result = cartRepository.existsByCustomer(user2);
        assertFalse(result);
    }

    @Test
    void testFindByCustomerUsername_shouldReturnCart_whenCartExists(){
        Optional<Cart> foundCart = cartRepository.findByCustomerUsername("testuser");
        assertNotNull(foundCart);
        assertEquals("testuser", foundCart.get().getCustomer().getUsername());
    }

    @Test
    void testFindByCustomerUsername_shouldReturnEmpty_WhenCartNotExists(){
        Optional<Cart> foundCart = cartRepository.findByCustomerUsername(user2.getUsername());
        assertTrue(foundCart.isEmpty());
    }
}
