package com.taskManager.taskapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskDto {

    public TaskDto(){}

    public TaskDto(String title, String description, LocalDate dueDate,String status){
        this.title = title;
        this.description= description;
        this.dueDate = dueDate;
        this.status = status;
    }

    private Long id;

    @NotNull(message = "vous devez saisir un titre à la tâche")
    private String title;

    private String description;

    @NotNull(message="vous devez entrer une date d'expiration")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private String status;

    @NotNull(message = "l'utilsateur qui détient cette tâche doit être spécifié")
    private Long userId;
}
