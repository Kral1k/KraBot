package com.kral1k.krabot.modal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ModalDispatcher<T> {
    private final Map<String, Consumer<T>> executorMap = new HashMap<>();

    public void register(String id, Consumer<T> executor) {
        executorMap.put(id, executor);
    }

    protected void execute(String id, T interaction) {
        executorMap.get(id).accept(interaction);
    }
}
