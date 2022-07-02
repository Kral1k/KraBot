package com.kral1k.krabot.button;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ButtonDispatcher<T> {
    private final Map<String, Consumer<T>> executorMap = new HashMap<>();

    public void register(String id, Consumer<T> executor) {
        executorMap.put(id, executor);
    }

    protected void execute(String id, T interaction) throws ButtonException {
        Consumer<T> consumer = executorMap.get(id);
        if (consumer == null) throw new ButtonNotFoundException();
        consumer.accept(interaction);
    }
}
