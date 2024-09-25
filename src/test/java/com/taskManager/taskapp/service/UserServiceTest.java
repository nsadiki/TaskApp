package com.taskManager.taskapp.service;


import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.mapper.MapEntityToDto;
import com.taskManager.taskapp.repositories.UserRepository;
import com.taskManager.taskapp.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    MapEntityToDto mapEntityToDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserByEmailUserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");


        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(mapEntityToDto.mapUserEntitytoDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserByEmail("test@example.com");
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetUserByEmailUserDoesNotExist() {
        User user = new User();
        UserDto userDto = new UserDto();

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        when(mapEntityToDto.mapUserEntitytoDto(user)).thenThrow(new EntityNotFoundException("User not found"));
        assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(anyString()));
    }

    @Test
    void testGetAllUser() {
        User user1 = new User();
        User user2 = new User();
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(mapEntityToDto.mapUserListEntityToDto(List.of(user1,user2))).thenReturn(List.of(userDto1,userDto2));
        List<UserDto> result = userService.getAllUser();

        verify(userRepository, times(1)).findAll();
        verify(mapEntityToDto, times(1)).mapUserListEntityToDto(anyList());
        assertEquals(2, result.size());
    }

    @Test
    void testRegisterUser() {
        UserDto userDto = new UserDto();
        User user = new User();
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