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
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MapDtoToEntityTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MapDtoToEntity mapDtoToEntity;

    private TaskDto taskDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("John Doe");

        taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Test Task");
        taskDto.setStatus("In Progress");
        taskDto.setUserId(1L);
    }

    @Test
    void mapTaskDtotoEntityShouldMapTaskDtoToTaskEntity() {
        Task taskMock = new Task();
        taskMock.setId(1L);
        taskMock.setTitle("Test Task");
        taskMock.setStatus("In Progress");

        when(modelMapper.map(taskDto, Task.class)).thenReturn(taskMock);

        Task task = mapDtoToEntity.mapTaskDtotoEntity(taskDto);

        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals("In Progress", task.getStatus());
    }

    @Test
    void mapUserDtoToEntityShouldMapUserDtoToUserEntity() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("John Doe");

        when(modelMapper.map(userDto, User.class)).thenReturn(userMock);

        User user = mapDtoToEntity.mapUserDtoToEntity(userDto);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getUsername());
    }

    @Test
    void mapListOfTaskDtoToEntityShouldMapTaskDtoListToTaskEntityList() {
        Task taskMock = new Task();
        taskMock.setId(1L);
        taskMock.setTitle("Test Task");
        taskMock.setStatus("In Progress");

        when(modelMapper.map(taskDto, Task.class)).thenReturn(taskMock);

        List<Task> tasks = mapDtoToEntity.mapListOfTaskDtoToEntity(Arrays.asList(taskDto));

        verify(modelMapper, times(1)).map(any(),any());


        assertNotNull(tasks);
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        assertEquals("In Progress", tasks.get(0).getStatus());
    }

    @Test
    void mapListOfUserDtoToEntityShouldMapUserDtoListToUserEntityList() {
        User userMock = new User();
        userMock.setId(1L);
        userMock.setUsername("John Doe");

        when(modelMapper.map(userDto, User.class)).thenReturn(userMock);

        List<User> users = mapDtoToEntity.mapListOfUserDtoToEntity(Arrays.asList(userDto));

        verify(modelMapper, times(1)).map(any(),any());

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getUsername());
    }
}
