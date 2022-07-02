package com.kral1k.krabot.guild;

import com.kral1k.krabot.utils.PropertiesHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class GuildProperties extends PropertiesHandler {
    public final String youTubeApiToken;

    public GuildProperties(Properties properties) {
        super(properties);
        this.youTubeApiToken = this.getString("youTubeApiToken", "");
    }

    public static GuildProperties load(Path path) {
        GuildProperties properties = Files.exists(path) ? new GuildProperties(loadProperties(path)) : new GuildProperties(new Properties());
        properties.saveProperties(path, "guild property");
        return properties;
    }
}
