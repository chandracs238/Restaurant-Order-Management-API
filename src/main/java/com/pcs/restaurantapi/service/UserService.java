package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.ProfileDto;
import com.pcs.restaurantapi.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUsername(String username);
    List<UserDto> getAllUsers();
    UserDto updateUser(String username, UserDto userDto);
    void deleteUser(String username);
    ProfileDto getProfile(String username);
    ProfileDto updateProfile(String username, ProfileDto profileDto);
}
