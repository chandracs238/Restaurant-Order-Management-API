package com.pcs.restaurantapi.repository;

import com.pcs.restaurantapi.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findAllByCategory(String category);
}
