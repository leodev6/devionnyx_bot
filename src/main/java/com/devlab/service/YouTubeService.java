package com.devlab.service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class YouTubeService {
    private final YouTube youtube;
    private final String apiKey;
    private String lastVideoId;

    public YouTubeService(@Value("${youtube.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.youtube = new YouTube.Builder(
            new NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            request -> {}
        ).setApplicationName("telegram-bot").build();
    }

    public VideoInfo getLatestVideo(String channelId) throws IOException {
        YouTube.Search.List search = youtube.search().list(Arrays.asList("id", "snippet"));
        search.setKey(apiKey);
        search.setChannelId(channelId);
        search.setOrder("date");
        search.setMaxResults(1L);
        search.setType(Arrays.asList("video"));

        SearchListResponse response = search.execute();
        List<SearchResult> items = response.getItems();

        if (items.isEmpty()) return null;

        SearchResult video = items.get(0);
        String videoId = video.getId().getVideoId();

        if (lastVideoId != null && lastVideoId.equals(videoId)) {
            return null;
        }

        lastVideoId = videoId;
        return new VideoInfo(
            video.getSnippet().getTitle(),
            "https://www.youtube.com/watch?v=" + videoId,
            video.getSnippet().getThumbnails().getHigh().getUrl(),
            video.getSnippet().getDescription()
        );
    }

    public record VideoInfo(String title, String url, String thumbnail, String description) {}
}

