package com.kral1k.krabot.utils;

import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public abstract class PropertiesHandler {
    private static final Logger LOGGER = JDALogger.getLog(PropertiesHandler.class);
    protected Properties properties;

    public PropertiesHandler(Properties properties) {
        this.properties = properties;
    }

    public static Properties loadProperties(Path path) {
        Properties properties = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            LOGGER.error("Failed to load properties from file: {}", path);
        }
        return properties;
    }

    public void saveProperties(Path path, String comments) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            properties.store(writer, comments);
        } catch (IOException e) {
            LOGGER.error("Failed to store properties to file: {}", path);
        }
    }

    public String getString(String key, String defaultValue) {
        String property = properties.getProperty(key);
        String string = this.firstNonNull(property, defaultValue);
        properties.put(key, string);
        return string;
    }

    public <T> T firstNonNull(T first, T second) {
        if (first != null) return first;
        if (second != null) return second;
        throw new NullPointerException("Both parameters are null");
    }
}
