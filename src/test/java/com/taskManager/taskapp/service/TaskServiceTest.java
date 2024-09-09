package com.taskManager.taskapp.service;


import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.mapper.MapDtoToEntity;
import com.taskManager.taskapp.repositories.TaskRepository;
import com.taskManager.taskapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private MapDtoToEntity mapDtoToEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        TaskDto taskDto = new TaskDto("Title", "Description", LocalDateTime.now(), "In Progress");
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setStatus(taskDto.getStatus());

        when(mapDtoToEntity.mapTaskDtotoEntity(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(taskDto);

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
        TaskDto taskDto = new TaskDto("Updated Title", "Updated Description", LocalDateTime.now(), "Completed");
        Task existingTask = new Task();
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setDueDate(LocalDateTime.now().minusDays(1));
        existingTask.setStatus("In Progress");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task updatedTask = taskService.updateTask(taskId, taskDto);

        assertEquals(taskDto.getTitle(), updatedTask.getTitle());
        assertEquals(taskDto.getDescription(), updatedTask.getDescription());
        assertEquals(taskDto.getDueDate(), updatedTask.getDueDate());
        assertEquals(taskDto.getStatus(), updatedTask.getStatus());
    }

    @Test
    void testGetTaskByUser() {
        Long userId = 1L;
        Task task = new Task();
        task.setTitle("Task Title");
        task.setDescription("Task Description");
        task.setDueDate(LocalDateTime.now());
        task.setStatus("In Progress");

        when(taskRepository.findByUserId(userId)).thenReturn(List.of(task));

        List<Task> tasks = taskService.getTaskByUser(userId);

        assertEquals(1, tasks.size());
        assertEquals(task.getTitle(), tasks.get(0).getTitle());
        assertEquals(task.getDescription(), tasks.get(0).getDescription());
    }
}

