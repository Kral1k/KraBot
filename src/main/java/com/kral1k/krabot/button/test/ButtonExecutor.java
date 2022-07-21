package com.kral1k.krabot.button.test;

import com.kral1k.krabot.button.Source;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.utils.Executor;
import com.kral1k.krabot.utils.ExecutionException;

import java.util.function.Predicate;

public class ButtonExecutor {
    private final String id;
    private final Predicate<Source> predicate;
    private final Executor<ButtonInteraction> executor;
    private int pressCount;

    public ButtonExecutor(String id, Predicate<Source> predicate, Executor<ButtonInteraction> executor, int pressCount) {
        this.id = id;
        this.predicate= predicate;
        this.executor = executor;
        this.pressCount = pressCount;
    }

    public String getId() {
        return id;
    }

    public void execute(ButtonInteraction interaction) throws ExecutionException {
        Source source = interaction.getMember() != null ? interaction.getMember() : interaction.getUser();
        if (predicate.test(source) || source.hasPermission(PermissionRole.DEVELOPER)) {
            executor.execute(interaction);
            if (pressCount > 0) {
                this.pressCount -= 1;
                if (pressCount <= 0) ButtonManager.unregister(id);
            }
        } else interaction.replyTranslatable("button.permission").setEphemeral(true).queue();
    }
}
