package com.taskManager.taskapp.dto;

import lombok.Data;

@Data
public class UserDto {

    public UserDto(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;

    }

    public UserDto() {

    }

    private Long id;

    private String username;

    private String email;

    private String password;



}
