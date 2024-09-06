package com.taskManager.taskapp.controllers;


import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("t/ask")
public class TaskHandlerController {

    @Autowired
    private TaskService taskService;


    @GetMapping("/{userId}")
    public List<Task> getTasksofAUser(@PathVariable Long userId) {
        return taskService.getTaskByUser(userId);
    }

    @PutMapping("/update/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody TaskDto task) {
        return taskService.updateTask(id, task);
    }

    @PostMapping("/create")
    public Task createTask(@RequestBody TaskDto task) {
        return taskService.createTask(task);
    }

    @DeleteMapping("/deleteTask/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}











