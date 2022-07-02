package com.kral1k.krabot.button;

import com.google.gson.Gson;
import com.kral1k.krabot.permission.Perms;

public class ButtonData {
    protected static final Gson GSON = new Gson();
    public final String id;
    public final Perms perms;

    public ButtonData(String id, Perms permission) {
        this.id = id;
        this.perms = permission;
    }

    public static ButtonData from(String s) {
        return GSON.fromJson(s, ButtonData.class);
    }
}
