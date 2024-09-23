package com.taskManager.taskapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskManager.taskapp.controllers.UserController;
import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void registerUserShouldReturnCreatedUser() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("testUser");
        userDto.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("test@example.com");
        savedUser.setUsername("testUser");
        savedUser.setPassword("password");

        when(userService.registerUser(org.mockito.Mockito.any(UserDto.class))).thenReturn(savedUser);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testUser"));

        verify(userService, times(1)).registerUser(org.mockito.Mockito.any(UserDto.class));
    }

    @Test
    void deleteUserShouldInvokeDeleteUserService() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/user/delete/1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void getUserShouldReturnUserDto() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        userDto.setUsername("testUser");

        when(userService.getUserByEmail("test@example.com")).thenReturn(userDto);

        // Act & Assert
        mockMvc.perform(get("/user/getUser/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("testUser"));

        verify(userService, times(1)).getUserByEmail("test@example.com");
    }

    @Test
    void getAllUserShouldReturnListOfUsers() throws Exception {
        // Arrange
        UserDto user1 = new UserDto();
        user1.setEmail("user1@example.com");
        user1.setUsername("user1");

        UserDto user2 = new UserDto();
        user2.setEmail("user2@example.com");
        user2.setUsername("user2");

        List<UserDto> users = Arrays.asList(user1, user2);

        when(userService.getAllUser()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/user/getAllUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email").value("user1@example.com"))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].email").value("user2@example.com"))
                .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userService, times(1)).getAllUser();
    }
}
