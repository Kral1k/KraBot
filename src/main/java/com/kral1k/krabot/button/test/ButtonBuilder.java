package com.kral1k.krabot.button.test;

import com.kral1k.krabot.button.Source;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.utils.Executor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;

import java.util.UUID;
import java.util.function.Predicate;

public class ButtonBuilder {
    private final String id = UUID.randomUUID().toString();
    private ButtonStyle style = ButtonStyle.PRIMARY;
    private String label = "unknown";
    private boolean disabled = false;
    private Emoji emoji = null;
    private int pressCount = -1;
    private Predicate<Source> predicate = source -> true;
    private Executor<ButtonInteraction> executor = interaction -> {};


    public ButtonBuilder setStyle(ButtonStyle style) {
        this.style = style;
        return this;
    }

    public ButtonBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public ButtonBuilder setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public ButtonBuilder setEmoji(Emoji emoji) {
        this.emoji = emoji;
        return this;
    }

    public ButtonBuilder setPressCount(int count) {
        this.pressCount = count;
        return this;
    }

    public ButtonBuilder predicate(Predicate<Source> predicate) {
        this.predicate = predicate;
        return this;
    }

    public ButtonBuilder predicate(String userId) {
        this.predicate = source -> source.has(userId);
        return this;
    }
    public ButtonBuilder predicate(PermissionRole permissionRole) {
        this.predicate = source -> source.hasPermission(permissionRole);
        return this;
    }
    public ButtonBuilder predicate(Permission permission) {
        this.predicate = source -> source.hasPermission(permission);
        return this;
    }

    public ButtonBuilder execute(Executor<ButtonInteraction> executor) {
        this.executor = executor;
        return this;
    }

    private Button createButton() {
        return new ButtonImpl(id, label, style, disabled, emoji);
    }

    private ButtonExecutor createExecutor() {
        return new ButtonExecutor(id, predicate, executor, pressCount);
    }

    public Button build() {
        ButtonManager.register(this.createExecutor());
        return this.createButton();
    }
}
