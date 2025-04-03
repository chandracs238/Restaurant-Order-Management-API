package com.pcs.restaurantapi.repository;


import com.pcs.restaurantapi.model.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MenuItemRepositoryTest {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private MenuItem menuItem1, menuItem2;

    @BeforeEach
    void setup(){
        menuItem1 = new MenuItem(null, "Pizza", BigDecimal.valueOf(500), "JunkFood", "Enjoy you gona love it!", true, LocalDateTime.now(), LocalDateTime.now());
        testEntityManager.persistAndFlush(menuItem1);
        menuItem2 = new MenuItem(null, "Burgar", BigDecimal.valueOf(500), "JunkFood", "Enjoy you gona love it!", true, LocalDateTime.now(), LocalDateTime.now());
        testEntityManager.persistAndFlush(menuItem2);
    }

    @Test
    void testFindAllByCategory_shouldReturnMenuItemFromThatCategory_WhenMenuItemsAvailable(){
        List<MenuItem> junkFoodList = menuItemRepository.findAllByCategory("JunkFood");

        assertEquals(2, junkFoodList.size());
        assertEquals("Burgar", junkFoodList.get(1).getName());
    }

    @Test
    void testFindAllByCategory_shouldReturnEmptyList_whenNoMenuItemsOfThatCatogery(){
        List<MenuItem> sweats = menuItemRepository.findAllByCategory("Sweats");

        assertTrue(sweats.isEmpty());
    }
}
