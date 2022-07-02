package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class ClearButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("clear", interaction -> {
            Data data = Data.from(interaction.getComponentId());
            interaction.getChannel().getIterableHistory()
                    .skipTo(interaction.getMessageIdLong())
                    .takeAsync(data.amount)
                    .thenAccept(interaction.getChannel()::purgeMessages);
            interaction.getMessage().delete().queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, int amount) {
        return Button.of(style, new Gson().toJson(new Data("clear", permission, amount)), label);
    }

    private static class Data extends ButtonData {
        private final int amount;

        public Data(String id, Perms permission, int amount) {
            super(id, permission);
            this.amount = amount;
        }

        public static Data from(String componentId) {
            return GSON.fromJson(componentId, Data.class);
        }
    }
}
