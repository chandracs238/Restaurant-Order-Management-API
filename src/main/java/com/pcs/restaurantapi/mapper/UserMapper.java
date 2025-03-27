package com.pcs.restaurantapi.mapper;

import com.pcs.restaurantapi.dto.ProfileDto;
import com.pcs.restaurantapi.dto.UserDto;
import com.pcs.restaurantapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public ProfileDto toProfileDto(User user){
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(user.getUsername());
        profileDto.setEmail(user.getEmail());
        profileDto.setDob(user.getDob());
        profileDto.setName(user.getName());
        return profileDto;
    }

    public User toEntity(UserDto userDto){
        if (userDto == null){
            return null;
        }
        User user = new User();
        user.setUsername(user.getUsername());
        user.setPassword(user.getPassword());
        return user;
    }


}
