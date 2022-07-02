package com.kral1k.krabot.language;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslatableText {
    private static final Pattern ARG_FORMAT = Pattern.compile("%([o%])([1-9])?");
    private final String key;
    private final Object[] objects;

    public TranslatableText(String key, Object... objects) {
        this.key = key;
        this.objects = objects;
    }

    public String translate(Language language) {
        String message = language.get(key);
        return this.match(message);
    }

    private String match(String message) {
        Matcher matcher = ARG_FORMAT.matcher(message);
        StringBuilder builder = new StringBuilder();

        try {
            int i = 0;
            int start;
            int matcherEnd;
            for (start = 0; matcher.find(start); start = matcherEnd) {
                int matcherStart = matcher.start();
                matcherEnd = matcher.end();
                String string;
                if (matcherStart > start) {
                    string = message.substring(start, matcherStart);
                    builder.append(string);
                }
                string = matcher.group(1);
                if (string.equals("%")) {
                    builder.append("%");
                } else {
                    string = matcher.group(2);
                    int index = string != null ? Integer.parseInt(string) - 1 : i++;
                    if (objects.length > index) {
                        builder.append(objects[index]);
                    } else builder.append("?");
                }
            }
            if (start < message.length()) {
                String string = message.substring(start);
                builder.append(string);
            }
        } catch (Throwable throwable) {
            throw new IllegalArgumentException("Translation ERROR");
        }
        return builder.toString();
    }

    public String translateDefault() {
        return this.translate(Language.getInstance());
    }
}
