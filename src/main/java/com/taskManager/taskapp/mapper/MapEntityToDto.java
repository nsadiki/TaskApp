package com.taskManager.taskapp.mapper;

import com.taskManager.taskapp.dto.TaskDto;
import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.entities.User;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MapEntityToDto {

    ModelMapper modelMapper = new ModelMapper();

    private static final Logger logger = LoggerFactory.getLogger((MapEntityToDto.class));


    public TaskDto mapTaskEntityToDto(Task task){
        TaskDto taskDto = modelMapper.map(task, TaskDto.class);
        logger.info("TEST BANGER : " + task.getTitle());
        if (task.getUser() != null) {
            taskDto.setUserId(task.getUser().getId());
        }else{
            taskDto.setTitle("RATE");
        }
        return taskDto;
    }

    public UserDto mapUserEntitytoDto (User user){
        return modelMapper.map(user, UserDto.class );
    }


    public List<TaskDto> mapTaskListEntityToDto(List<Task> tasks){
        logger.info("TEST MAPPER LIST : " + tasks.get(0).getTitle());
        List<TaskDto> tasksDto= tasks.stream().map(task -> {
            TaskDto dto = modelMapper.map(task, TaskDto.class);
            if (task.getUser()!=null){
                dto.setUserId(task.getUser().getId());
            }
            logger.info("TEST DE L4OBJET FINI : " + dto.getTitle());
            return dto;
        }).collect(Collectors.toList());
        return tasksDto;
    }

    public List<UserDto> mapUserListEntityToDto(List<User> users){
        logger.info("taille en sortie du dto  : " + users.size());
        return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }
}
