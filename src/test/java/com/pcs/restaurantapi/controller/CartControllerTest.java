package com.pcs.restaurantapi.controller;

import com.pcs.restaurantapi.dto.CartItemDto;
import com.pcs.restaurantapi.security.JwtUtil;
import com.pcs.restaurantapi.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartServiceImpl cartService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "pcs", roles = "CUSTOMER")
    void testGetCartItems() throws Exception {
        List<CartItemDto> cartItems = List.of(new CartItemDto(1L, 1L, 2L, 100));

        when(cartService.getAllCartItems("pcs")).thenReturn(cartItems);

        mockMvc.perform(get("/api/v1/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].menuItemId").value(2));  // Fixed incorrect field
    }

    @Test
    void testGetCartItems_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/cart"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetCartItemsWithCustomUser() throws Exception {
        // ✅ Create UserDetails correctly
        UserDetails userDetails = User.builder()
                .username("chandra")
                .password("chandra")
                .roles("CUSTOMER")
                .build();

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(context);

        // ✅ Fix username in mocked service call
        List<CartItemDto> cartItems = List.of(new CartItemDto(2L, 1L, 3L, 50));
        when(cartService.getAllCartItems("chandra")).thenReturn(cartItems);

        // ✅ Fix JSONPath validation
        mockMvc.perform(get("/api/v1/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))  // Fix array size check
                .andExpect(jsonPath("$[0].id").value(2)) // Ensure field matches DTO
                .andExpect(jsonPath("$[0].menuItemId").value(3L));
    }

}

