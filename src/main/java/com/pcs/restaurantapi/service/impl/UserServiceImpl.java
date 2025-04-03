package com.pcs.restaurantapi.service.impl;

import com.pcs.restaurantapi.dto.ProfileDto;
import com.pcs.restaurantapi.dto.UserDto;
import com.pcs.restaurantapi.exception.RoleNotFoundException;
import com.pcs.restaurantapi.exception.UserAlreadyExistsException;
import com.pcs.restaurantapi.exception.UserNotFoundException;
import com.pcs.restaurantapi.mapper.UserMapper;
import com.pcs.restaurantapi.model.Role;
import com.pcs.restaurantapi.model.User;
import com.pcs.restaurantapi.repository.RoleRepository;
import com.pcs.restaurantapi.repository.UserRepository;
import com.pcs.restaurantapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }

        Role defaultRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RoleNotFoundException("Default role not found"));

        User user = new User();
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(defaultRole);

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userMapper.toUserDto(findUserByUsername(username));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public UserDto updateUser(String username, UserDto userDto) {
        User user = findUserByUsername(username);

        user.setUsername(userDto.getUsername());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException("User not found.");
        }
        userRepository.deleteByUsername(username);
    }

    @Override
    public ProfileDto getProfile(String username) {
        return userMapper.toProfileDto(findUserByUsername(username));
    }

    @Override
    public ProfileDto updateProfile(String username, ProfileDto profileDto) {
        User user = findUserByUsername(username);

        user.setName(profileDto.getName());
        user.setDob(profileDto.getDob());
        user.setUsername(profileDto.getUsername());
        user.setEmail(profileDto.getEmail());

        return userMapper.toProfileDto(userRepository.save(user));
    }

    // ----------------- Private Helper Methods -----------------

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }
}
