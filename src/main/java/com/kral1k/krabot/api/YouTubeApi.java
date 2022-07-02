package com.kral1k.krabot.api;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class YouTubeApi {
    public static final Gson GSON = new Gson();

    public static void search(String apiKey, String name, BiConsumer<Integer, Data> consumer) {
        String url = String.format("https://www.googleapis.com/youtube/v3/search?key=%s&part=snippet&type=video&videoCategoryId=10&regionCode=RU&q=%s", apiKey, name.replace(" ", "%20"));
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        CompletableFuture<HttpResponse<String>> future = HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        future.thenAccept(response -> {
            int statusCode = response.statusCode();
            Data data = statusCode != 200 ? null : GSON.fromJson(response.body(), Data.class);
            consumer.accept(statusCode, data);
        });
    }

    public static class Data {
        private Item[] items;

        public Item[] getItems() {
            return items;
        }

        public Item getItem(int index) {
            return items[index];
        }

        public boolean isEmpty() {
            return items.length == 0;
        }

        public static class Item {

            private Id id;
            private Snippet snippet;

            public Id getId() {
                return id;
            }

            public Snippet getSnippet() {
                return snippet;
            }

            public static class Id {
                private String videoId;

                public String getVideoId() {
                    return videoId;
                }
            }

            public static class Snippet {
                private String title;

                public String getTitle() {
                    return title;
                }
            }
        }
    }
}
