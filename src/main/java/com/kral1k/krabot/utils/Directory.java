package com.kral1k.krabot.utils;

public enum Directory {
    ROOT("."),
    USERDATA("userdata"),
    GUILDS("guilds"),
    LANGUAGES("lang");

    private final String path;

    Directory(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
