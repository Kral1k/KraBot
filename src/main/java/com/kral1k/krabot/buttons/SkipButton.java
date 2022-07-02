package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

public class SkipButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("skip", interaction -> {
            interaction.getGuild().getMusicPlayer().skip();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.GREEN);
            embedBuilder.setTitle(Text.translatable("player.skip"));
            interaction.editMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission) {
        return Button.of(style, new Gson().toJson(new ButtonData("skip", permission)), label);
    }
}
