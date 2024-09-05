package com.taskManager.taskapp.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private LocalDateTime dueDate;

    private String Status;
}
