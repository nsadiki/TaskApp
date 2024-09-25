package com.taskManager.taskapp.services;

import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.mapper.MapDtoToEntity;
import com.taskManager.taskapp.mapper.MapEntityToDto;
import com.taskManager.taskapp.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapDtoToEntity mapDtoToEntity;

    @Autowired
    private MapEntityToDto mapEntityToDto;


    private static final Logger logger = LoggerFactory.getLogger((UserService.class));


    public UserDto getUserByEmail(String email){

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        return mapEntityToDto.mapUserEntitytoDto(user);
    }

    public List<UserDto> getAllUser(){


        List<UserDto> dto = mapEntityToDto.mapUserListEntityToDto(userRepository.findAll());
        logger.info("user list legnth : " + dto.size());
        return dto ;
    }

    @Transactional
    public User registerUser(UserDto user){

        return userRepository.save(mapDtoToEntity.mapUserDtoToEntity(user));

    }

    @Transactional
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
