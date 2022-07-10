package com.kral1k.krabot.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class YouTubeSearch {
    public static final String URL = "https://www.googleapis.com/youtube/v3/search";

    private final String apiKey;
    protected final Map<String, String> map = new HashMap<>();

    public YouTubeSearch(String apiKey, String title) {
        this.apiKey = apiKey;
        map.put("q", title.replace(" ", "%20"));
    }

    public static YouTubeSearch newRequest(String apiKey, String title) {
        return new YouTubeSearch(apiKey, title);
    }

    public YouTubeSearch setType(Type type) {
        map.put("type", type.toString());
        return this;
    }

    public YouTubeSearch setRegionCode(String regionCode){
        map.put("regionCode", regionCode.toUpperCase());
        return this;
    }

    public YouTubeSearch setCategory(Category category) {
        map.put("videoCategoryId", category.id);
        return this;
    }

    public YouTubeSearch setVideoEmbeddable() {
        map.put("videoEmbeddable", "");
        return this;
    }

    public CompletableFuture<YouTubeSearchResponse> sendAsync() {
        StringJoiner stringJoiner = new StringJoiner("&");
        stringJoiner.add(URL + "?key=" + apiKey + "&part=snippet");
        map.forEach((key, value) -> stringJoiner.add(key + "=" + value));
        URI uri = URI.create(stringJoiner.toString());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).thenApply(YouTubeSearchResponse::new);
    }

    public void sendAsync(BiConsumer<Integer, YouTubeSearchResponse.Body> consumer) {
        this.sendAsync().thenAccept(response -> consumer.accept(response.statusCode, response.body));
    }

    public enum Type {
        VIDEO,
        PLAYLIST,
        CHANNEL;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public enum Category {
        MUSIC("10");

        public final String id;

        Category(String id) {
            this.id = id;
        }
    }
}
