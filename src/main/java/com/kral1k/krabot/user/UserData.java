package com.kral1k.krabot.user;

import com.kral1k.krabot.utils.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UserData extends Data {

    public UserData(Path path) {
        super(path);
    }

    public static UserData initialize(Path path) {
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                UserData data = GSON.fromJson(reader, UserData.class);
                data.path = path;
                return data;
            } catch (IOException e) {
                LOGGER.error("Failed to initialize UserData to file: {}", path);
            }
        }
        UserData data = new UserData(path);
        data.serialize();
        return data;
    }
}
