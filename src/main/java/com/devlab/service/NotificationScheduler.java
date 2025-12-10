package com.devlab.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {
    private final YouTubeService youtubeService;
    private final TelegramService telegramService;

    @Value("${youtube.channel.id}")
    private String channelId;

    public NotificationScheduler(YouTubeService youtubeService, TelegramService telegramService) {
        this.youtubeService = youtubeService;
        this.telegramService = telegramService;
    }

    @Scheduled(fixedRate = 600000)
    public void checkNewVideo() {
        try {
            var video = youtubeService.getLatestVideo(channelId);
            if (video != null) {
                String message = String.format(
                    "<b>🎥 Nouvelle vidéo disponible !</b>\n\n" +
                    "<b>%s</b>\n\n" +
                    "%s\n\n" +
                    "<a href=\"%s\">▶️ Regarder la vidéo</a>",
                    video.title(),
                    video.description().length() > 200 
                        ? video.description().substring(0, 200) + "..." 
                        : video.description(),
                    video.url()
                );
                telegramService.sendPhotoToGroup(video.thumbnail(), message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

