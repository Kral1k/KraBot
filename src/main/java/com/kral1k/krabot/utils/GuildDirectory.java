package com.kral1k.krabot.utils;

public enum GuildDirectory {
    ROOT(""),
    MEMBERDATA("memberdata"),
    GAMES("games"),
    GAME_PUZZLE("games/puzzle");

    private final String path;

    GuildDirectory(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
