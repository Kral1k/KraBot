package com.kral1k.krabot.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

public class GsonHelper {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T deserialize(String string, Class<T> tClass) {
        return GSON.fromJson(string, tClass);
    }
    public static <T> T deserialize(Reader reader, Class<T> tClass) {
        return GSON.fromJson(reader, tClass);
    }
    public static void serialize(Writer writer, Object object) {
        GSON.toJson(object, writer);
    }

    public static void serialize(Writer writer, JsonElement jsonElement) {
        GSON.toJson(jsonElement, writer);
    }

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static JsonObject fromJson(Reader reader) {
        return GSON.fromJson(reader, JsonObject.class);
    }
}
