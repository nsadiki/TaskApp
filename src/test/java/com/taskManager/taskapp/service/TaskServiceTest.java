package com.taskManager.taskapp.service;


import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.mapper.MapDtoToEntity;
import com.taskManager.taskapp.mapper.MapEntityToDto;
import com.taskManager.taskapp.repositories.TaskRepository;
import com.taskManager.taskapp.repositories.UserRepository;
import com.taskManager.taskapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;



@SpringBootTest
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private MapDtoToEntity mapDtoToEntity;

    @Mock
    private MapEntityToDto mapEntityToDto;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        TaskDto taskDto = new TaskDto("Title", "Description", LocalDate.now(), "In Progress");
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setStatus(taskDto.getStatus());
        User user = new User();
        user.setId(1L);



        when(taskRepository.save(task)).thenReturn(task);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapEntityToDto.mapTaskEntityToDto(task)).thenReturn(taskDto);

        TaskDto createdTask = taskService.createTask(taskDto);

        verify(taskRepository, times(1)).save(any());
        verify(userRepository, times(1)).findById(anyLong());
        verify(mapEntityToDto, times(1)).mapTaskEntityToDto(any());

        assertEquals(task.getTitle(), createdTask.getTitle());
        assertEquals(task.getDescription(), createdTask.getDescription());
        assertEquals(task.getDueDate(), createdTask.getDueDate());
        assertEquals(task.getStatus(), createdTask.getStatus());
    }

    @Test
    void testDeleteTask() {
        Long taskId = 1L;
        doNothing().when(taskRepository).deleteById(taskId);
        taskService.deleteTask(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void testUpdateTask() {
        Long taskId = 1L;
        Task task = new Task();
        TaskDto taskDto = new TaskDto("Updated Title", "Updated Description", LocalDate.now(), "Completed");
        task.setTitle("Title");
        task.setDescription("ancienne description");
        task.setDueDate(LocalDate.now().minusDays(1));
        task.setStatus("In Progress");

        when(mapEntityToDto.mapTaskEntityToDto(task)).thenReturn(taskDto);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDto updatedTask = taskService.updateTask(taskId, taskDto);

        verify(mapEntityToDto, times(1)).mapTaskEntityToDto(any());
        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).save(any());

        assertEquals(taskDto.getTitle(), updatedTask.getTitle());
        assertEquals(taskDto.getDescription(), updatedTask.getDescription());
        assertEquals(taskDto.getDueDate(), updatedTask.getDueDate());
        assertEquals(taskDto.getStatus(), updatedTask.getStatus());
    }

    @Test
    void testGetTaskByUser() {
        Long userId = 1L;
        Task task1 = new Task();
        Task task2 = new Task();
        TaskDto taskDto1 = new TaskDto();
        TaskDto taskDto2 = new TaskDto();


        // initialisation des champs pour tester le premier élément de la liste
        task1.setTitle("Title");
        task1.setDescription("Description");
        task1.setDueDate(LocalDate.now());
        task1.setStatus("In Progress");

        taskDto1.setTitle("Title");
        taskDto1.setDescription("Description");
        taskDto1.setDueDate(LocalDate.now());
        taskDto1.setStatus("In Progress");

        TaskDto dto  = new TaskDto();

        when(taskRepository.findByUserId(userId)).thenReturn(List.of(task1, task2));
        when(mapEntityToDto.mapTaskListEntityToDto(List.of(task1,task2))).thenReturn(List.of(taskDto1,taskDto2));

        List<TaskDto> tasks = taskService.getTaskByUser(userId);


        verify(taskRepository, times(1)).findByUserId(anyLong());
        verify(mapEntityToDto, times(1)).mapTaskListEntityToDto(anyList());

        assertEquals(2, tasks.size());
        assertEquals(task1.getTitle(), tasks.get(0).getTitle());
        assertEquals(task1.getDescription(), tasks.get(0).getDescription());
    }
}

