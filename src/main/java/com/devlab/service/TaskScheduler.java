package com.devlab.service;

import com.devlab.entity.User;
import com.devlab.repository.TaskRepository;
import com.devlab.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TaskScheduler {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TelegramService telegramService;

    public TaskScheduler(TaskRepository taskRepository, UserRepository userRepository, TelegramService telegramService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.telegramService = telegramService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyTask() {
        taskRepository.findRandomTask().ifPresent(task -> {
            List<User> users = userRepository.findAll();
            String message = String.format(
                "<b>📚 Exercice du jour</b>\n\n" +
                "<b>Catégorie:</b> %s\n\n" +
                "%s\n\n" +
                "Utilisez /correction pour voir la solution",
                task.getCategory(),
                task.getMessage()
            );
            users.forEach(user -> telegramService.sendMessage(user.getTelegramId(), message));
        });
    }
}

