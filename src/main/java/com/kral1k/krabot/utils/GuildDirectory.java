package com.kral1k.krabot.utils;

public enum GuildDirectory {
    ROOT("."),
    MEMBERDATA("memberdata"),
    GAMES("games"),
    GAME_PUZZLE("games/puzzle");

    public final String name;

    GuildDirectory(String name) {
        this.name = name;
    }
}
