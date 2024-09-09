package com.taskManager.taskapp.service;

import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.repositories.TaskRepository;
import com.taskManager.taskapp.repositories.UserRepository;
import com.taskManager.taskapp.services.ReportingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.*;

@SpringBootTest
public class ReportingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private ReportingService reportingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateReportForUserShouldCreateReportFile() throws IOException {
        Long userId = 1L;
        User user = new User();
        user.setUsername("JohnDoe");

        Task task1 = new Task();
        task1.setStatus("Completed");

        Task task2 = new Task();
        task2.setStatus("Pending");

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(taskRepository.findByUserId(userId)).thenReturn(tasks);

        try (BufferedWriter writer = mock(BufferedWriter.class)) {
            doNothing().when(writer).write(anyString());
            doNothing().when(writer).close();

            try (FileWriter fileWriter = mock(FileWriter.class)) {
                reportingService.generateReportForUser(userId);
                verify(fileWriter, times(1)).write(anyString());
            }
        }
    }

    @Test
    void generateReportForUserUserNotFoundShouldNotCreateReport() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());
        reportingService.generateReportForUser(userId);
        verify(taskRepository, never()).findByUserId(userId);
    }

    @Test
    void generateReportForUserNoTasksShouldNotCreateReport() {
        Long userId = 1L;
        User user = new User();
        user.setUsername("JohnDoe");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(taskRepository.findByUserId(userId)).thenReturn(new ArrayList<>());
        reportingService.generateReportForUser(userId);
        verify(taskRepository, times(1)).findByUserId(userId);
    }

    @Test
    void generateReportForUserWriteReportToFileShouldHandleIOException() throws IOException {
        Long userId = 1L;
        User user = new User();
        user.setUsername("JohnDoe");

        Task task = new Task();
        task.setStatus("Completed");

        List<Task> tasks = List.of(task);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(taskRepository.findByUserId(userId)).thenReturn(tasks);
        try (BufferedWriter writer = mock(BufferedWriter.class)) {
            doThrow(new IOException("Write error")).when(writer).write(anyString());
            doNothing().when(writer).close();

            try (FileWriter fileWriter = mock(FileWriter.class)) {
                reportingService.generateReportForUser(userId);
            }
        }
    }
}