package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setup(){
        role = new Role("dummyRole");
        roleRepository.save(role);
    }

    @Test
    void testFindByName_shouldReturnRole_whenRoleExists(){
        Optional<Role> foundRole = roleRepository.findByName("dummyRole");
        assertTrue(foundRole.isPresent());
    }

    @Test
    void testFindByName_shouldReturnEmpty_whenRoleDoesNotExists(){
        Optional<Role> foundRole = roleRepository.findByName("ADMIN");
        assertTrue(foundRole.isEmpty());
    }
}
