package com.devlab.controller;

import com.devlab.entity.Task;
import com.devlab.entity.User;
import com.devlab.repository.TaskRepository;
import com.devlab.repository.UserRepository;
import com.devlab.service.AssistantService;
import com.devlab.service.TelegramService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/webhook")
public class TelegramWebhookController {
    private final TelegramService telegramService;
    private final AssistantService assistantService;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Value("${formation.link:https://youtu.be/x6yepLvHctk}")
    private String formationLink;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public TelegramWebhookController(TelegramService telegramService, AssistantService assistantService,
                                   UserRepository userRepository, TaskRepository taskRepository) {
        this.telegramService = telegramService;
        this.assistantService = assistantService;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping
    public void handleUpdate(@RequestBody Update update) {
        if (!update.hasMessage()) return;

        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();

        if (text == null) return;

        if (text.startsWith("/start")) {
            handleStart(chatId, message.getFrom().getFirstName());
        } else if (text.startsWith("/correction")) {
            handleCorrection(chatId);
        } else if (text.startsWith("/")) {
            telegramService.sendMessage(chatId, "Commande non reconnue. Utilisez /start pour commencer.");
        } else {
            handleMessage(chatId, text, message.getFrom().getFirstName());
        }
    }

    private void handleStart(Long chatId, String name) {
        userRepository.findByTelegramId(chatId)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setTelegramId(chatId);
                newUser.setName(name);
                return userRepository.save(newUser);
            });
        telegramService.sendMessage(chatId, "Bonjour " + name + " ! Je suis l'assistant DevLab. Posez-moi une question !");
    }

    private void handleCorrection(Long chatId) {
        Optional<Task> task = taskRepository.findRandomTask();
        if (task.isPresent() && task.get().getSolution() != null) {
            telegramService.sendMessage(chatId, "<b>Solution:</b>\n\n" + task.get().getSolution());
        } else {
            telegramService.sendMessage(chatId, "Aucune solution disponible pour le moment.");
        }
    }

    private void handleMessage(Long chatId, String text, String name) {
        Optional<User> userOpt = userRepository.findByTelegramId(chatId);
        
        if (userOpt.isPresent() && userOpt.get().getWaitingForEmail()) {
            handleEmail(chatId, text, userOpt.get());
        } else if (text.toLowerCase().contains("formation")) {
            User user = userOpt.orElseGet(() -> {
                User newUser = new User();
                newUser.setTelegramId(chatId);
                newUser.setName(name);
                return userRepository.save(newUser);
            });
            user.setWaitingForEmail(true);
            userRepository.save(user);
            telegramService.sendMessage(chatId, "Veuillez envoyer votre adresse email pour accéder à la formation gratuite.");
        } else {
            String response = assistantService.processMessage(text);
            telegramService.sendMessage(chatId, response);
        }
    }

    private void handleEmail(Long chatId, String text, User user) {
        if (EMAIL_PATTERN.matcher(text).matches()) {
            user.setEmail(text);
            user.setWaitingForEmail(false);
            userRepository.save(user);
            telegramService.sendMessage(chatId, 
                "Merci ! Voici le lien vers la formation gratuite :\n" + formationLink);
        } else {
            telegramService.sendMessage(chatId, "Email invalide. Veuillez réessayer.");
        }
    }
}

