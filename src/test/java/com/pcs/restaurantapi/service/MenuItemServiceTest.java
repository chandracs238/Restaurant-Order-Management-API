package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.MenuItemDto;
import com.pcs.restaurantapi.exception.MenuItemNotFoundException;
import com.pcs.restaurantapi.mapper.MenuItemMapper;
import com.pcs.restaurantapi.model.MenuItem;
import com.pcs.restaurantapi.repository.MenuItemRepository;
import com.pcs.restaurantapi.service.impl.MenuItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private MenuItem menuItem;
    private MenuItemDto menuItemDto;

    @BeforeEach
    void setup(){
        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Dosa");
        menuItem.setCategory("Tiffin");
        menuItem.setPrice(BigDecimal.valueOf(60));

        menuItemDto = new MenuItemDto();
        menuItemDto.setName(menuItem.getName());
        menuItemDto.setPrice(menuItem.getPrice());
        menuItemDto.setCategory(menuItem.getCategory());
    }

    @Test
    void testCreateMenuItem_shouldCreateMenuItem_WhenNewMenuItemAdd(){
        when(menuItemMapper.toEntity(menuItemDto)).thenReturn(menuItem);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);
        when(menuItemMapper.toMenuItemDto(menuItem)).thenReturn(menuItemDto);

        MenuItemDto result = menuItemService.createMenuItem(menuItemDto);

        assertNotNull(result);
        assertEquals("Dosa", result.getName());
        verify(menuItemRepository).save(any(MenuItem.class));
        verify(menuItemMapper).toMenuItemDto(any(MenuItem.class));
        verify(menuItemMapper).toEntity(any(MenuItemDto.class));
    }

    @Test
    void testCreateMenuItem_ValidationFailure() {
        // Given: Invalid input
        MenuItemDto invalidDto = new MenuItemDto(null, null, null, null); // Invalid name & price

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuItemService.createMenuItem(invalidDto));
        verify(menuItemRepository, never()).save(any()); // Ensure no DB call happens
    }

    @Test
    void testCreateMenuItem_RepositoryCalledOnce() {
        // Given
        MenuItemDto inputDto = new MenuItemDto("Pizza", BigDecimal.valueOf(400), "JunkFood", "Bad for health");
        MenuItem menuItem1 = new MenuItem(2L, "Pizza", BigDecimal.valueOf(400), "JunkFood", "Bad for health",true ,LocalDateTime.now(), LocalDateTime.now());

        when(menuItemMapper.toEntity(inputDto)).thenReturn(menuItem1);
        when(menuItemRepository.save(menuItem1)).thenReturn(menuItem1);
        when(menuItemMapper.toMenuItemDto(menuItem1)).thenReturn(inputDto);

        // When
        menuItemService.createMenuItem(inputDto);

        // Then
        verify(menuItemRepository, times(1)).save(menuItem1);
    }

    @Test
    void testCreateMenuItem_NullInput() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> menuItemService.createMenuItem(null));
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void testCreateMenuItem_RepositoryFailure() {
        // Given
        MenuItemDto inputDto = new MenuItemDto("Pizza", BigDecimal.valueOf(400), "JunkFood", "Bad for health");
        MenuItem menuItem1 = new MenuItem(2L, "Pizza", BigDecimal.valueOf(400), "JunkFood", "Bad for health",true ,LocalDateTime.now(), LocalDateTime.now());

        when(menuItemMapper.toEntity(inputDto)).thenReturn(menuItem1);
        when(menuItemRepository.save(menuItem1)).thenThrow(new DataAccessException("DB Error") {});

        // When & Then
        assertThrows(DataAccessException.class, () -> menuItemService.createMenuItem(inputDto));
    }

    @Test
    void testGetMenuItemById_shouldGetMenuItem_whenMenuItemExists(){
        // Given
        when(menuItemRepository.findById(1L)).thenReturn(Optional.ofNullable(menuItem));
        when(menuItemMapper.toMenuItemDto(menuItem)).thenReturn(menuItemDto);

        // When
        MenuItemDto result = menuItemService.getMenuItemById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Dosa", result.getName());
        verify(menuItemRepository).findById(1L);
        verify(menuItemMapper).toMenuItemDto(any(MenuItem.class));
    }

    @Test
    void testGetMenuItemById_shouldThrowException_whenMenuItemNotFound(){
        // Given
        when(menuItemRepository.findById(200L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(MenuItemNotFoundException.class, () -> menuItemService.getMenuItemById(200L));
        verify(menuItemMapper, never()).toMenuItemDto(any(MenuItem.class));
    }

    @Test
    void testGetAllMenuItems_shouldReturnAllMenuItems_whenMenuItemsAvailable(){
        // Given
        when(menuItemRepository.findAll()).thenReturn(List.of(menuItem));
        when(menuItemMapper.toMenuItemDto(menuItem)).thenReturn(menuItemDto);

        // When
        List<MenuItemDto> result = menuItemService.getAllMenuItems();

        // Then
        assertEquals(1, result.size());
        verify(menuItemRepository).findAll();
    }

    @Test
    void testGetAllMenuItems_ShouldReturnEmptyList_whenNoMenuItemFound(){
        // Given
        when(menuItemRepository.findAll()).thenReturn(List.of());

        // When
        List<MenuItemDto> result = menuItemService.getAllMenuItems();

        // Then
        assertTrue(result.isEmpty());
        verify(menuItemRepository).findAll();
    }

}
