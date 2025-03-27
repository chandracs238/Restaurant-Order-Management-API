package com.pcs.restaurantapi.service.impl;

import com.pcs.restaurantapi.dto.MenuItemDto;
import com.pcs.restaurantapi.exception.MenuItemNotFoundException;
import com.pcs.restaurantapi.mapper.MenuItemMapper;
import com.pcs.restaurantapi.model.MenuItem;
import com.pcs.restaurantapi.repository.MenuItemRepository;
import com.pcs.restaurantapi.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;

    @PreAuthorize("hasRole('MANAGER')")
    @Override
    public MenuItemDto createMenuItem(MenuItemDto menuItemDto) {
        validateMenuItemDto(menuItemDto);
        MenuItem menuItem = menuItemMapper.toEntity(menuItemDto);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toMenuItemDto(savedMenuItem);
    }

    @Override
    public MenuItemDto getMenuItemById(Long menuItemId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("MenuItem not found"));
        return menuItemMapper.toMenuItemDto(menuItem);
    }

    @Override
    public List<MenuItemDto> getAllMenuItems() {
        return menuItemRepository.findAll().stream().map(menuItemMapper::toMenuItemDto).toList();
    }

    @Override
    public List<MenuItemDto> getAllMenuItemsByCatogery(String catogery) {
        return menuItemRepository.findAllByCategory(catogery).stream().map(menuItemMapper::toMenuItemDto).toList();
    }

    @Override
    public MenuItemDto updateMenuItem(Long menuItemId, MenuItemDto menuItemDto) {
        validateMenuItemDto(menuItemDto);
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("MenuItem not found"));
        menuItem.setName(menuItemDto.getName());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setCategory(menuItemDto.getCategory());
        menuItem.setDescription(menuItemDto.getDescription());

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return menuItemMapper.toMenuItemDto(savedMenuItem);
    }

    @Override
    public void deleteMenuItem(Long menuItemId) {
        menuItemRepository.deleteById(menuItemId);
    }

    @Override
    public MenuItemDto updatePriceOfMenuItem(Long menuItemId, int price) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException("MenuItem not found"));
        menuItem.setPrice(BigDecimal.valueOf(price));
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toMenuItemDto(savedMenuItem);
    }

    private void validateMenuItemDto(MenuItemDto menuItemDto) {
        if (menuItemDto.getName() == null || menuItemDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Menu item name cannot be empty");
        }
        if (menuItemDto.getPrice() == null || menuItemDto.getPrice().intValue() < 0) {
            throw new IllegalArgumentException("Menu item price must be positive");
        }
    }

}
