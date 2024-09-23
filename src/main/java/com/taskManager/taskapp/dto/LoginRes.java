package com.taskManager.taskapp.dto;


import lombok.Data;

@Data
public class LoginRes {
    

    private String email;
    private String token;

    public LoginRes(String email, String token) {
        this.email = email;
        this.token= token;
    }
}
