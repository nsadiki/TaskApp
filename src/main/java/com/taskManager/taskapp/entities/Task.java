package com.taskManager.taskapp.entities;


import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name="tasks")
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String title;

    @Column(nullable = false, unique = false)
    private String description;

    @Column(nullable = false, unique = false)
    private LocalDate dueDate;

    @Column(nullable = false, unique = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
