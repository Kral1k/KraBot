package com.kral1k.krabot.api;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class Wiki {
    public static final Gson GSON = new Gson();

    public static void request(String name, BiConsumer<Integer, Data> consumer) {
        URI uri = URI.create("https://ru.wikipedia.org/api/rest_v1/page/summary/" + name.replace("\s", "%20"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        CompletableFuture<HttpResponse<String>> future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        future.thenAccept(response -> {
            int statusCode = response.statusCode();
            Data data = statusCode != 200 ? null : GSON.fromJson(response.body(), Data.class);
            consumer.accept(statusCode, data);
        });
    }

    public static class Data {
        public String extract;
    }
}
