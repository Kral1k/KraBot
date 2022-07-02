package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

public class DeleteButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("delete", interaction -> {
            interaction.getMessage().delete().queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission) {
        return Button.of(style, new Gson().toJson(new ButtonData("delete", permission)), label);
    }
}
