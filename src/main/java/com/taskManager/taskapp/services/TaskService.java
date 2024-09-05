package com.taskManager.taskapp.services;


import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.mapper.MapDtoToEntity;
import com.taskManager.taskapp.repositories.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {


    @Autowired
    public TaskRepository taskRepository;

    @Autowired
    public MapDtoToEntity mapDtoToEntity;


    @Transactional
    public Task createTask(TaskDto task){
        return taskRepository.save(mapDtoToEntity.mapTaskDtotoEntity(task));
    }

    @Transactional
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    @Transactional
    public Task updateTask(Long id, TaskDto task) {
        Optional<Task> existingTask = taskRepository.findById(id);
        return existingTask.map(t -> {
                    t.setTitle(task.getTitle());
                    t.setDescription(task.getDescription());
                    t.setDueDate(task.getDueDate());
                    t.setStatus(task.getStatus());
                    return taskRepository.save(t);
                }).orElse(null);

    }

    public List<Task> getTaskByUser(Long userId){
       return taskRepository.findByUserId(userId);
    }

}
