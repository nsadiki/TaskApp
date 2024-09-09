package com.taskManager.taskapp.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {

    public TaskDto(String title, String description, LocalDateTime dueDate,String status){
        this.title = title;
        this.description= description;
        this .dueDate = dueDate;
        this.status = status;
    }

    private Long id;

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private String status;

    private Long userId;
}
