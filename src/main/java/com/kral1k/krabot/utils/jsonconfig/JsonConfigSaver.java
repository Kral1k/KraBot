package com.kral1k.krabot.utils.jsonconfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConfigSaver<T extends JsonConfig> {
    private static final Logger LOGGER = JDALogger.getLog(JsonConfigSaver.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Class<T> class_;
    private final Path path;
    private T config;

    public JsonConfigSaver(Class<T> class_, Path path) {
        this.class_ = class_;
        this.path = path;
    }

    public T get() {
        return config;
    }

    public T getConfig() {
        return config;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(path)) {
            GSON.toJson(config, writer);
        } catch (IOException exception) {
            LOGGER.error("Failed to save data to file: {}", path);
        }
    }

    public JsonConfigSaver<T> load() {
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                config = GSON.fromJson(reader, class_);
            } catch (IOException exception) {
                LOGGER.error("Failed to load data to file: {}", path);
                this.create();
            }
        } else this.create();
        this.save();
        return this;
    }

    private void create() {
        try {
            config = class_.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException exception) {
            throw new RuntimeException(exception);
        }
    }
}
