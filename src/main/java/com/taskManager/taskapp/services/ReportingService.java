package com.taskManager.taskapp.services;


import com.taskManager.taskapp.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingService {


    @Autowired
    private TaskRepository taskRepository;


    public void generateReport(String status){

        StringBuilder reportContent = new StringBuilder();



    }
}
