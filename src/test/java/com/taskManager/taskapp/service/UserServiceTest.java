package com.taskManager.taskapp.service;


import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.mapper.MapDtoToEntity;
import com.taskManager.taskapp.repositories.UserRepository;
import com.taskManager.taskapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MapDtoToEntity mapDtoToEntity;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByEmailUserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("test@example.com");
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetUserByEmailUserDoesNotExist() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        User result = userService.getUserByEmail("nonexistent@example.com");
        assertNull(result);
    }

    @Test
    void testGetAllUser() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userService.getAllUser();
        assertEquals(2, result.size());
    }

    @Test
    void testRegisterUser() {
        UserDto userDto = new UserDto();
        User user = new User();
        when(mapDtoToEntity.mapUserDtoToEntity(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.registerUser(userDto);
        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);
        verify(userRepository).deleteById(userId);
    }
    }