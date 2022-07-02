package com.kral1k.krabot.language;

public interface Text {
    static String translatable(String key, Object... objects) {
        return new TranslatableText(key, objects).translateDefault();
    }
}
