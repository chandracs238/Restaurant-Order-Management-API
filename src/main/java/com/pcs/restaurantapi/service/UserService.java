package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.ProfileDto;
import com.pcs.restaurantapi.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long userId);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);
    ProfileDto getProfile(String username);
    ProfileDto updateProfile(String username, ProfileDto profileDto);
}
