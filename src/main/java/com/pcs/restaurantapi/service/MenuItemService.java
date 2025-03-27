package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.MenuItemDto;

import java.util.List;

public interface MenuItemService {
    MenuItemDto createMenuItem(MenuItemDto menuItemDto);
    MenuItemDto getMenuItemById(Long menuItemId);
    List<MenuItemDto> getAllMenuItems();
    List<MenuItemDto> getAllMenuItemsByCatogery(String catogery);
    MenuItemDto updateMenuItem(Long menuItemId, MenuItemDto menuItemDto);
    void deleteMenuItem(Long menuItemId);
    MenuItemDto updatePriceOfMenuItem(Long menuItemId, int price);
}
