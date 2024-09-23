package com.taskManager.taskapp.controllers;


import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/auth/register")
    public User registerUser(@Valid @RequestBody UserDto user){
        return userService.registerUser(user);
    }

    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @GetMapping("/user/getUser/{email}")
    public UserDto getUser(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/user/getAllUser")
    public List<UserDto> getAllUser(){

        return userService.getAllUser();
    }
}
