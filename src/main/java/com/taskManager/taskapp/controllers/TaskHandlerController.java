package com.taskManager.taskapp.controllers;


import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskHandlerController {

    @Autowired
    private TaskService taskService;


    @GetMapping("/{userId}")
    public List<TaskDto> getTasksofAUser(@PathVariable Long userId) {
        return taskService.getTaskByUser(userId);
    }

    @PutMapping("/update/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto task) {
        return taskService.updateTask(id, task);
    }

    @PostMapping("/create")
    public TaskDto createTask(@Valid @RequestBody TaskDto task) {
        return taskService.createTask(task);
    }

    @DeleteMapping("/deleteTask/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}











