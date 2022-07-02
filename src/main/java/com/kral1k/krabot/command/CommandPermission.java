package com.kral1k.krabot.command;

public interface CommandPermission<T> {
    boolean accept(T t);
}
