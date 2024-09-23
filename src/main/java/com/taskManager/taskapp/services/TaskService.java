package com.taskManager.taskapp.services;

import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.mapper.MapDtoToEntity;
import com.taskManager.taskapp.mapper.MapEntityToDto;
import com.taskManager.taskapp.repositories.TaskRepository;
import com.taskManager.taskapp.repositories.UserRepository;
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
    public UserRepository userRepository;

    @Autowired
    public MapDtoToEntity mapDtoToEntity;

    @Autowired
    public MapEntityToDto mapEntityToDto;


    @Transactional
    public TaskDto createTask(TaskDto taskDto){

        Task task = taskRepository.save(mapDtoToEntity.mapTaskDtotoEntity(taskDto));
        Optional<User> user = userRepository.findById(taskDto.getUserId());
        if(user.isPresent()){
            task.setUser(user.get());
        }else{
            throw new RuntimeException("User not found" + taskDto.getUserId());
        }
        return mapEntityToDto.mapTaskEntityToDto(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    @Transactional
    public TaskDto updateTask(Long id, TaskDto task) {
        Optional<Task> existingTask = taskRepository.findById(id);
        return existingTask.map(t -> {
                    t.setTitle(task.getTitle());
                    t.setDescription(task.getDescription());
                    t.setDueDate(task.getDueDate());
                    t.setStatus(task.getStatus());
                    return mapEntityToDto.mapTaskEntityToDto(taskRepository.save(t));
                }).orElse(null);

    }

    public List<TaskDto> getTaskByUser(Long userId){
       return mapEntityToDto.mapTaskListEntityToDto(taskRepository.findByUserId(userId));
    }

}
