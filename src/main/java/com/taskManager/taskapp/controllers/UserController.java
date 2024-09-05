package com.taskManager.taskapp.controllers;


import com.taskManager.taskapp.dto.UserDto;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping("/user/register")
    public User registerUser(@RequestParam UserDto user){
        return userService.registerUser(user);
    }

    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }

    @GetMapping("/user/getUser/{email}")
    public User getUser(@PathVariable String email){
        return userService.getUserByEmail("email");
    }
}
