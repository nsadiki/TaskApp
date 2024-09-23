package com.taskManager.taskapp.services;

import com.taskManager.taskapp.entities.Task;
import com.taskManager.taskapp.entities.User;
import com.taskManager.taskapp.repositories.TaskRepository;
import com.taskManager.taskapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableAsync
public class ReportingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Async
    public void generateReportForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<Task> tasks = taskRepository.findByUserId(userId);

        if (!(user == null) && !tasks.isEmpty()) {
            Map<String, Long> statusCounts = tasks.stream()
                    .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

            StringBuilder reportContent = new StringBuilder();
            reportContent.append("Nom de l'utilisateur: ").append(user.getUsername()).append("\n");
            reportContent.append("Nombre de tâche : ").append(tasks.size()-1).append("\n");
            reportContent.append("Nombre de tâche par status:\n");
            statusCounts.forEach((status, count) ->
                    reportContent.append("Status: ").append(status).append(count).append("\n")
            );

            writeReportToFile(userId, reportContent.toString());
        }
    }

    private void writeReportToFile(Long userId, String content) {
        String fileName = "task_status_report_user_" + userId + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}