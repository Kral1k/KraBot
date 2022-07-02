package com.kral1k.krabot.guild;

import com.kral1k.krabot.utils.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GuildData extends Data {

    public String gTtsName = "ru-RU-Wavenet-D";

    public GuildData(Path path) {
        super(path);
    }

    public static GuildData initialize(Path path) {
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                GuildData data = GSON.fromJson(reader, GuildData.class);
                data.path = path;
                return data;
            } catch (IOException e) {
                LOGGER.error("Failed to initialize GuildData to file: {}", path);
            }
        }

        GuildData data = new GuildData(path);
        data.serialize();
        return data;
    }
}
