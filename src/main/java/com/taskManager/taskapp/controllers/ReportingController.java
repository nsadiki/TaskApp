package com.taskManager.taskapp.controllers;

import com.taskManager.taskapp.services.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportingController {

    @Autowired
    private ReportingService reportingService;

    @GetMapping("/generate/{userId}")
    public String generateReport(@PathVariable Long userId) {
        reportingService.generateReportForUser(userId);
        return "Report generation started.";
    }
}
