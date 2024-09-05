package com.taskManager.taskapp.mapper;

import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MapEntityToDto {

    ModelMapper modelMapper = new ModelMapper();


    public TaskDto mapTaskEntityToDto(Task task){
        return modelMapper.map(task, TaskDto.class);
    }

    public UserDto mapUserEntitytoDto (User user){
        return modelMapper.map(user, UserDto.class );
    }


    public List<TaskDto> mapTaskListEntityToDto(List<Task> tasks){
        return Optional.ofNullable(tasks).stream().map(item -> modelMapper.map(item, TaskDto.class)).toList();
    }

    public List<UserDto> mapUserListEntityToDto(List<User> users){
        return Optional.ofNullable(users).stream().map(item -> modelMapper.map(item, UserDto.class)).toList();
    }
}
