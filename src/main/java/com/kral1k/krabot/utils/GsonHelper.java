package com.kral1k.krabot.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.io.Reader;

public class GsonHelper {
    public static final Gson GSON = new Gson();

    public static <T> T deserialize(String string, Class<T> tClass) {
        return GSON.fromJson(string, tClass);
    }
    public static <T> T deserialize(Reader reader, Class<T> tClass) {
        return GSON.fromJson(reader, tClass);
    }
    public static JsonObject fromJson(InputStreamReader reader) {
        return GSON.fromJson(reader, JsonObject.class);
    }
}
