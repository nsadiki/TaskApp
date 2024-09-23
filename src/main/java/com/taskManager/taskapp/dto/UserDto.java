package com.taskManager.taskapp.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

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

    @NotNull(message="Le nom d'utilisateur est obligatoire")
    private String username;

    @NotNull(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotNull(message = "Vous devez saisir un mot de passe")
    @Size(min= 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;



}
