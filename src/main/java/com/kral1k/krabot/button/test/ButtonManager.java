package com.kral1k.krabot.button.test;

import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.utils.ExecutionException;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ButtonManager {
    public static final Map<String, ButtonExecutor> BUTTON_MAP = new HashMap<>();

    public static void onPress(ButtonInteraction interaction) {
        try {
            ButtonExecutor executor = BUTTON_MAP.get(interaction.getButtonId());
            if (executor != null) executor.execute(interaction);
            else {
                interaction.replyTranslatable("button.notFound").setEphemeral(true).queue();
                interaction.getMessage().delete().queue(null, error -> {});
            }
        } catch (ExecutionException exception) {
            sendThrowMessage(interaction, exception.getMessage());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            sendThrowMessage(interaction, Text.translatable("button.error"));
        }
    }

    private static void sendThrowMessage(ButtonInteraction interaction, String message) {
        if (interaction.isAcknowledged()) interaction.getHook().sendMessage(message).delay(5, TimeUnit.SECONDS).flatMap(Message::delete).queue(null, error -> {});
        else interaction.reply(message).setEphemeral(true).queue();
    }

    public static ButtonBuilder newBuilder() {
        return new ButtonBuilder();
    }

    public static void register(ButtonExecutor executor) {
        BUTTON_MAP.put(executor.getId(), executor);
    }

    public static void unregister(String id) {
        BUTTON_MAP.remove(id);
    }
}
