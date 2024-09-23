package com.taskManager.taskapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.taskManager.taskapp.controllers.TaskHandlerController;
import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

class TaskHandlerControllerTest {


    private static final Logger logger = LoggerFactory.getLogger((TaskHandlerControllerTest.class));


    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskHandlerController taskHandlerController;

    private MockMvc mockMvc;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskHandlerController).build();
    }

    @Test
    void getTasksofAUserShouldReturnListOfTasks() throws Exception {
        // Arrange
        TaskDto task1 = new TaskDto();
        task1.setTitle("Task 1");

        TaskDto task2 = new TaskDto();
        task2.setTitle("Task 2");

        List<TaskDto> tasks = Arrays.asList(task1, task2);

        when(taskService.getTaskByUser(1L)).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(taskService, times(1)).getTaskByUser(1L);
    }

    @Test
    void updateTaskShouldReturnUpdatedTask() throws Exception {
        // Arrange
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("Updated Task");

        when(taskService.updateTask(eq(1L), org.mockito.Mockito.any(TaskDto.class))).thenReturn(taskDto);

        // Act & Assert
        mockMvc.perform(put("/task/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Task\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));

        verify(taskService, times(1)).updateTask(eq(1L), org.mockito.Mockito.any(TaskDto.class));
    }

    @Test
    void createTaskShouldReturnCreatedTask() throws Exception {
        // Arrange
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle("New Task");
        taskDto.setUserId(1L);
        taskDto.setDueDate(LocalDate.parse("2024-09-15"));
        taskDto.setDescription("test");
        taskDto.setStatus("Backlog");

        when(taskService.createTask(org.mockito.Mockito.any(TaskDto.class))).thenReturn(taskDto);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Act & Assert
        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Task"));

        verify(taskService, times(1)).createTask(org.mockito.Mockito.any(TaskDto.class));
    }

    @Test
    void deleteTaskShouldInvokeDeleteTaskService() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/task/deleteTask/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask(1L);
    }
}
