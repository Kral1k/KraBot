package com.kral1k.krabot.permission;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.utils.GsonHelper;
import com.kral1k.krabot.utils.GuildDirectory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class PermissionManager {

    private JsonObject jsonObject = new JsonObject();
    public PermissionManager(Guild guild) {
        Path path = guild.getDirectory(GuildDirectory.ROOT).resolve("permission.json");
        if (Files.exists(path)) this.load(path);
        else this.create(path);
    }

    public String getRoleId(PermissionRole permissionRole) {
        return jsonObject.has(permissionRole.toString()) ? jsonObject.get(permissionRole.toString()).getAsString() : "null";
    }

    private void initialize() {
        for (PermissionRole permissionRole : PermissionRole.values()) {
            String name = permissionRole.toString();
            if (!jsonObject.has(name)) jsonObject.addProperty(name, "00000");
        }
    }

    public void create(Path path) {
        this.initialize();
        this.save(path);
    }

    public void save(Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            GsonHelper.serialize(writer, jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            this.jsonObject = GsonHelper.deserialize(reader, JsonObject.class);
            this.initialize();
            this.save(path);
        } catch (IOException e) {
            this.create(path);
        }
    }
}
