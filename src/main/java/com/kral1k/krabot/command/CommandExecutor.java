package com.kral1k.krabot.command;

public interface CommandExecutor<T> {
    void run(T t) throws CommandException;
}
