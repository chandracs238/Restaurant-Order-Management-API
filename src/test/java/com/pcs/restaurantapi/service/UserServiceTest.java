package com.pcs.restaurantapi.service;

import com.pcs.restaurantapi.dto.ProfileDto;
import com.pcs.restaurantapi.dto.UserDto;
import com.pcs.restaurantapi.exception.UserAlreadyExistsException;
import com.pcs.restaurantapi.exception.UserNotFoundException;
import com.pcs.restaurantapi.mapper.UserMapper;
import com.pcs.restaurantapi.model.Role;
import com.pcs.restaurantapi.model.User;
import com.pcs.restaurantapi.repository.RoleRepository;
import com.pcs.restaurantapi.repository.UserRepository;
import com.pcs.restaurantapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role role;
    private UserDto userDto;
    private List<User> users;
    private List<UserDto> userDtos;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setId(1L);
        role.setName("CUSTOMER");

        user = new User();
        user.setId(1L);
        user.setUsername("pcs");
        user.setPassword("pcs");
        user.setRole(role);

        userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());

        users = List.of(user);
        userDtos = List.of(userDto);
    }

    @Test
    void testCreateUser_shouldCreateUser_whenUsernameIsUnique() {
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("pcs");
        when(roleRepository.findByName("CUSTOMER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals("pcs", result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_shouldThrowException_whenUsernameAlreadyExists() {
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDto));

        // Ensure that no database operations occur if the user already exists
        verifyNoInteractions(passwordEncoder, roleRepository, userMapper);
    }


    @Test
    void testCreateUser_shouldThrowException_whenRoleNotFound() {
        when(userRepository.existsByUsername(userDto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("hashPassword");
        when(roleRepository.findByName("MANAGER")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.createUser(userDto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUserByUsername_shouldReturnUserDto_whenUserFound() {
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserByUsername("pcs");

        assertNotNull(result);
        assertEquals("pcs", result.getUsername());
        verify(userRepository).findByUsername("pcs");
    }

    @Test
    void testGetUserByUsername_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("baba")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("baba"));

        verify(userRepository).findByUsername("baba");
        verify(userMapper, never()).toUserDto(any());
    }

    @Test
    void testGetAllUsers_shouldReturnAllUsers_whenUsersFound() {
        when(userRepository.findAll()).thenReturn(users);
        for (int i = 0; i < users.size(); i++) {
            when(userMapper.toUserDto(users.get(i))).thenReturn(userDtos.get(i));
        }

        List<UserDto> result = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(users.size())).toUserDto(any(User.class));

        assertFalse(result.isEmpty());
        assertEquals(users.size(), result.size());
        assertEquals(userDtos.get(0).getUsername(), result.get(0).getUsername());
    }

    @DisplayName("Should update user when user is found")
    @Test
    void testUpdateUser_shouldUpdateUser_whenUserFound() {
        UserDto updateDto = new UserDto();
        updateDto.setUsername("Karun");
        updateDto.setPassword("karun");

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updateDto.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(any(User.class))).thenReturn(updateDto);

        UserDto updatedDto = userService.updateUser("pcs", updateDto);

        assertNotNull(updatedDto);
        assertEquals("Karun", updatedDto.getUsername());
        verify(passwordEncoder, times(1)).encode(updateDto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_shouldReturnError_whenUserNotFound() {
        UserDto updateDto = new UserDto();
        updateDto.setUsername("Karun");
        updateDto.setPassword("karun");
        when(userRepository.findByUsername("baba")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser("baba", updateDto));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDeleteUser_shouldDeleteUser_whenUserFound() {
        when(userRepository.existsByUsername("pcs")).thenReturn(true);
        doNothing().when(userRepository).deleteByUsername("pcs");

        userService.deleteUser("pcs");

        verify(userRepository, times(1)).deleteByUsername("pcs");
    }

    @Test
    void testGetProfile_shouldReturnProfile_whenFound() {
        ProfileDto profileDto = new ProfileDto("pcs", "pcs@gmail.com", "chandra", LocalDate.now().minusYears(23).minusMonths(4));
        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(userMapper.toProfileDto(user)).thenReturn(profileDto);

        ProfileDto result = userService.getProfile("pcs");

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void testGetProfile_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getProfile("nonexistent"));

        verify(userRepository, times(1)).findByUsername("nonexistent");
        verify(userMapper, never()).toProfileDto(any());
    }

    @Test
    void testUpdateProfile_shouldUpdateProfile_whenUserFound() {
        ProfileDto updateProfileDto = new ProfileDto("Karun", "karun@gmail.com", "Karun Shekhar", LocalDate.of(2000, 5, 15));

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toProfileDto(any(User.class))).thenReturn(updateProfileDto);

        ProfileDto result = userService.updateProfile("pcs", updateProfileDto);

        assertNotNull(result);
        assertEquals("Karun", result.getUsername());
        assertEquals("karun@gmail.com", result.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_shouldNotEncodePassword_whenPasswordIsNull() {
        UserDto updateDto = new UserDto();
        updateDto.setUsername("Karun");
        updateDto.setPassword(null);  // No password update

        when(userRepository.findByUsername("pcs")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(any(User.class))).thenReturn(updateDto);

        UserDto updatedDto = userService.updateUser("pcs", updateDto);

        assertNotNull(updatedDto);
        assertEquals("Karun", updatedDto.getUsername());

        // Ensure password was NOT changed
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

}
