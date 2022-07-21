package com.kral1k.krabot.utils;

public interface Executor<T> {
    void execute(T t) throws ExecutionException;
}
