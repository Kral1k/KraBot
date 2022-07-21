package com.kral1k.krabot.button;

import com.kral1k.krabot.utils.ExecutionException;
import com.kral1k.krabot.utils.Executor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ButtonDispatcher<T> {
    private final Map<String, Executor<T>> executorMap = new HashMap<>();

    public void register(String id, Executor<T> executor) {
        executorMap.put(id, executor);
    }

    protected void execute(String id, T interaction) throws ExecutionException {
        Executor<T> consumer = executorMap.get(id);
        if (consumer == null) throw new ButtonNotFoundException();
        consumer.execute(interaction);
    }
}
