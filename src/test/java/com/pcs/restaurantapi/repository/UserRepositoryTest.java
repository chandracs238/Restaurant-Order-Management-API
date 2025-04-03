package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.exception.RoleNotFoundException;
import com.pcs.restaurantapi.model.Role;
import com.pcs.restaurantapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User user;
    private Role role;

    @BeforeEach
    void setup() {
        role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setRole(role);
        user = userRepository.save(user);
    }

    @Test
    void testFindByUsername(){
        Optional<User> foundUser = userRepository.findByUsername("username");
        assertTrue(foundUser.isPresent());
        assertEquals("username", foundUser.get().getUsername());
    }

    @Test
    void testExistByUsername(){
        boolean result = userRepository.existsByUsername("username");
        assertTrue(result);
    }

    @Test
    void testDeleteByUsername(){
        userRepository.deleteByUsername("username");
        Optional<User> deletedUser = userRepository.findByUsername("username");
        assertTrue(deletedUser.isEmpty());
    }
}
