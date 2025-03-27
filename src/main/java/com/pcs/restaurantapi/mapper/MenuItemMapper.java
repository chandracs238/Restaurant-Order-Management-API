package com.pcs.restaurantapi.mapper;

import com.pcs.restaurantapi.dto.MenuItemDto;
import com.pcs.restaurantapi.model.MenuItem;
import com.pcs.restaurantapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    public MenuItemDto toMenuItemDto(MenuItem menuItem){
        if (menuItem == null){
            return null;
        }
        MenuItemDto dto = new MenuItemDto();
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setCategory(menuItem.getCategory());
        return dto;
    }

    public MenuItem toEntity(MenuItemDto menuItemDto){
        if (menuItemDto == null){
            return null;
        }
        MenuItem entity = new MenuItem();
        entity.setName(menuItemDto.getName());
        entity.setDescription(menuItemDto.getDescription());
        entity.setPrice(menuItemDto.getPrice());
        entity.setCategory(menuItemDto.getCategory());
        return entity;
    }

}
