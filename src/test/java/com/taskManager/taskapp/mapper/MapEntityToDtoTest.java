package com.taskManager.taskapp.mapper;

import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapEntityToDtoTest {
    @Mock
    private Logger logger;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MapEntityToDto mapEntityToDto;

    private Task task;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("John Doe");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setUser(user);
    }

    @Test
    void mapTaskEntityToDtoShouldMapTaskToTaskDtoWhenUserExists() {
        TaskDto taskDtoMock = new TaskDto();
        taskDtoMock.setId(1L);
        taskDtoMock.setTitle("Test Task");
        taskDtoMock.setUserId(1L);

        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDtoMock);

        TaskDto taskDto = mapEntityToDto.mapTaskEntityToDto(task);

        assertNotNull(taskDto);
        assertEquals("Test Task", taskDto.getTitle());
        assertEquals(1L, taskDto.getUserId());

    }

    @Test
    void mapTaskEntityToDtoShouldSetTitleToRateWhenUserIsNull() {
        task.setUser(null);

        TaskDto taskDtoMock = new TaskDto();
        taskDtoMock.setId(1L);
        taskDtoMock.setTitle("RATE");

        when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDtoMock);

        TaskDto taskDto = mapEntityToDto.mapTaskEntityToDto(task);

        assertNotNull(taskDto);
        assertEquals("RATE", taskDto.getTitle());

    }

    @Test
    void mapUserEntitytoDtoShouldMapUserToUserDto() {
        UserDto userDtoMock = new UserDto();
        userDtoMock.setId(1L);
        userDtoMock.setUsername("John Doe");

        when(modelMapper.map(user, UserDto.class)).thenReturn(userDtoMock);

        UserDto userDto = mapEntityToDto.mapUserEntitytoDto(user);

        assertNotNull(userDto);
        assertEquals("John Doe", userDto.getUsername());
    }

    @Test
    void mapTaskListEntityToDtoShouldMapTaskListToTaskDtoList() {
        TaskDto taskDtoMock = new TaskDto();
        taskDtoMock.setId(1L);
        taskDtoMock.setTitle("Test Task");
        taskDtoMock.setUserId(1L);

        OngoingStubbing<TaskDto> taskDtoOngoingStubbing = when(modelMapper.map(task, TaskDto.class)).thenReturn(taskDtoMock);

        List<TaskDto> taskDtos = mapEntityToDto.mapTaskListEntityToDto(Arrays.asList(task));

        assertNotNull(taskDtos);
        assertEquals(1, taskDtos.size());
        assertEquals("Test Task", taskDtos.get(0).getTitle());
    }

    @Test
    void mapUserListEntityToDtoShouldMapUserListToUserDtoList() {
        UserDto userDtoMock = new UserDto();
        userDtoMock.setId(1L);
        userDtoMock.setUsername("John Doe");

        when(modelMapper.map(user, UserDto.class)).thenReturn(userDtoMock);

        List<UserDto> userDtos = mapEntityToDto.mapUserListEntityToDto(Arrays.asList(user));

        assertNotNull(userDtos);
        assertEquals(1, userDtos.size());
        assertEquals("John Doe", userDtos.get(0).getUsername());
    }
}
