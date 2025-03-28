package com.pcs.restaurantapi.controller;

import com.pcs.restaurantapi.dto.MenuItemDto;
import com.pcs.restaurantapi.service.impl.MenuItemServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemServiceImpl menuItemService;

    @GetMapping("/menu")
    public ResponseEntity<List<MenuItemDto>> getAllMenuItems(){
        return ResponseEntity.ok(menuItemService.getAllMenuItems());
    }

    @GetMapping("/menu/{menuItemId}")
    public ResponseEntity<MenuItemDto> getMenuItemById(@PathVariable Long menuItemId){
        return ResponseEntity.ok(menuItemService.getMenuItemById(menuItemId));
    }

    @PostMapping("/menu")
    public ResponseEntity<MenuItemDto> createMenuItem(@Valid @RequestBody MenuItemDto menuItemDto){
        MenuItemDto createdMenuItem = menuItemService.createMenuItem(menuItemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMenuItem);
    }

    @PutMapping("/menu/{menuItemId}")
    public ResponseEntity<?> updateMenuItem(@PathVariable Long menuItemId, @Valid @RequestBody MenuItemDto menuItemDto){
        return ResponseEntity.ok("Success");
    }

    @DeleteMapping("/menu/{menuItemId}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long menuItemId){
        return ResponseEntity.ok("success");
    }

}
