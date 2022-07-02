package com.kral1k.krabot;

import com.kral1k.krabot.utils.PropertiesHandler;
import net.dv8tion.jda.api.entities.Activity;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class BotProperties extends PropertiesHandler {
    public final String token;
    public final Activity.ActivityType activityType;
    public final String activityName;
    public final String activityUrl;

    public BotProperties(Properties properties) {
        super(properties);
        this.token = getString("token", "***");
        this.activityType = parseActivityType("activityType", Activity.ActivityType.PLAYING);
        this.activityName = getString("activityName", "minecraft");
        this.activityUrl = getString("activityUrl", "https://twitch.tv/Kral1k_");
    }

    public Activity.ActivityType parseActivityType(String key, Activity.ActivityType defaultValue) {
        String property = properties.getProperty(key);
        String string = this.firstNonNull(property, defaultValue.name());
        properties.put(key, string);
        try {
            return Activity.ActivityType.valueOf(string);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static BotProperties load(Path path) {
        BotProperties properties = Files.exists(path) ? new BotProperties(loadProperties(path)) : new BotProperties(new Properties());
        properties.saveProperties(path, "bot property");
        return properties;
    }
}
