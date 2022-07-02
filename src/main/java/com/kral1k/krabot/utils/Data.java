package com.kral1k.krabot.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Data {
    protected static final Logger LOGGER = JDALogger.getLog(Data.class);
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected transient Path path;

    public Data(Path path) {
        this.path = path;
    }

    public synchronized void serialize() {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            LOGGER.error("Failed to serialize data to file: {}", path);
        }
    }
}
