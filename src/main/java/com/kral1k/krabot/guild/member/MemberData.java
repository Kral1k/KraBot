package com.kral1k.krabot.guild.member;

import com.kral1k.krabot.utils.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MemberData extends Data {
    public double kraCoin = 0;
    public MemberData(Path path) {
        super(path);
    }

    public static MemberData initialize(Path path) {
        if (Files.exists(path)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                MemberData data = GSON.fromJson(reader, MemberData.class);
                data.path = path;
                return data;
            } catch (IOException e) {
                LOGGER.error("Failed to initialize MemberData to file: {}", path);
            }
        }
        MemberData data = new MemberData(path);
        data.serialize();
        return data;
    }
}
