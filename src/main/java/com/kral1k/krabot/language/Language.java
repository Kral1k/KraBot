package com.kral1k.krabot.language;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kral1k.krabot.Bot;
import com.kral1k.krabot.utils.Directory;
import com.kral1k.krabot.utils.GsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class Language {
    private static Language instance = Language.create();

    public static Language getInstance() {
        return instance;
    }

    public static Language create() {
        try (InputStream stream = Language.class.getResourceAsStream("/lang/ru_ru.json")) {
            return create(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Language create(InputStream stream) {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        load(stream, builder::put);
        final Map<String, String> map = builder.build();
        return new Language() {
            @Override
            public String get(String key) {
                return map.getOrDefault(key, key);
            }
        };
    }

    public static Language create(Path path) throws IOException {
        try (InputStream stream = Files.newInputStream(path)){
            return create(stream);
        }
    }

    public static Language createOrDefault(Path path) {
        if (Files.exists(path)) {
            try {
                return create(path);
            } catch (IOException ignore) {}
        }
        return create();
    }

    public static Language createOrDefault(String locale) {
        Path path = Bot.getDirectory(Directory.LANGUAGES).resolve(locale + ".json");
        return createOrDefault(path);
    }

    private static void load(InputStream stream, BiConsumer<String, String> consumer) {
        JsonObject jsonObject = GsonHelper.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8));
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue().getAsString());
        }
    }

    public abstract String get(String key);
}
