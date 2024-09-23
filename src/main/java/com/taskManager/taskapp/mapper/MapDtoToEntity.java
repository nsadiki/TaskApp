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
public class MapDtoToEntity {

    ModelMapper modelMapper = new ModelMapper();


    public Task mapTaskDtotoEntity (TaskDto taskDto){
        Task taskEntity = new Task();

        return modelMapper.map(taskDto, Task.class);
//        return Optional.ofNullable(taskDto).map(t -> {
//            taskEntity.setId(taskDto.getId());
//            taskEntity.setStatus(taskDto.getStatus());
//            taskEntity.setTitle(taskDto.getTitle());
//            taskEntity.setDescription(taskDto.getDescription());
//            return taskEntity;
//        }).orElse(null);
    }


   public  User mapUserDtoToEntity(UserDto userDto){
      return modelMapper.map(userDto, User.class);

   }

   public List<Task> mapListOfTaskDtoToEntity(List<TaskDto> tasks){
        return Optional.ofNullable(tasks).stream().map(item -> modelMapper.map(item, Task.class)).toList();
   }

   public List<User> mapListOfUserDtoToEntity(List<UserDto> users){
        return users.stream().map(user -> modelMapper.map(user, User.class))
                .toList();
    }

}
