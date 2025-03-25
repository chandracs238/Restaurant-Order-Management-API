package com.pcs.restaurantapi.service.impl;

import com.pcs.restaurantapi.dto.ProfileDto;
import com.pcs.restaurantapi.dto.UserDto;
import com.pcs.restaurantapi.exception.UserAlreadyExistsException;
import com.pcs.restaurantapi.exception.UserNotFoundException;
import com.pcs.restaurantapi.model.Role;
import com.pcs.restaurantapi.model.RoleName;
import com.pcs.restaurantapi.model.User;
import com.pcs.restaurantapi.repository.RoleRepository;
import com.pcs.restaurantapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Collections.singleton(role));

        User savedUser = userRepository.save(user);

        return "User created successfully";
    }

    public ProfileDto getProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found "));
        return new ProfileDto(user);
    }

    public ProfileDto updateProfile(String username, ProfileDto profileDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found "));
        user.setName(profileDto.getName());
        user.setDob(profileDto.getDob());
        user.setUsername(profileDto.getUsername());
        user.setEmail(profileDto.getEmail());
        userRepository.save(user);
        return new ProfileDto(user);
    }
}
