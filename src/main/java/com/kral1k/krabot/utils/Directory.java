package com.kral1k.krabot.utils;

public enum Directory {
    ROOT("."),
    USERDATA("userdata"),
    GUILDS("guilds"),
    LANGUAGES("lang");

    public final String name;

    Directory(String name) {
        this.name = name;
    }
}
