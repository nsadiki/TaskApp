package com.taskManager.taskapp.services;


import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.mapper.MapDtoToEntity;
import com.taskManager.taskapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MapDtoToEntity mapDtoToEntity;


    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUser(){
        return userRepository.findAll();
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
