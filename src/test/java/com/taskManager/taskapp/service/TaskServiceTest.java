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

        TaskDto createdTask = taskService.createTask(taskDto);

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
        Task existingTask = new Task();
        existingTask.setTitle("Title");
        existingTask.setDescription("ancienne description");
        existingTask.setDueDate(LocalDate.now().minusDays(1));
        existingTask.setStatus("In Progress");

        when(mapEntityToDto.mapTaskEntityToDto(task)).thenReturn(taskDto);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskDto updatedTask = taskService.updateTask(taskId, taskDto);

        assertEquals(taskDto.getTitle(), updatedTask.getTitle());
        assertEquals(taskDto.getDescription(), updatedTask.getDescription());
        assertEquals(taskDto.getDueDate(), updatedTask.getDueDate());
        assertEquals(taskDto.getStatus(), updatedTask.getStatus());
    }

    @Test
    void testGetTaskByUser() {
        Long userId = 1L;
        Task task = new Task();
        task.setTitle("Title");
        task.setDescription("Description");
        task.setDueDate(LocalDate.now());
        task.setStatus("In Progress");

        when(taskRepository.findByUserId(userId)).thenReturn(List.of(task));

        List<TaskDto> tasks = taskService.getTaskByUser(userId);

        assertEquals(1, tasks.size());
        assertEquals(task.getTitle(), tasks.get(0).getTitle());
        assertEquals(task.getDescription(), tasks.get(0).getDescription());
    }
}

