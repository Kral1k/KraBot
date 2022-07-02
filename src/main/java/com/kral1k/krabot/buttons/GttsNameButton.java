package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.permission.Perms;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

public class GttsNameButton {

    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("gttsName", interaction -> {
            Data data = Data.from(interaction.getComponentId());
            interaction.getGuild().getGTts().setName(data.name);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Gtts name changed to: " + data.name);
            embedBuilder.setColor(Color.GREEN);
            interaction.editMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, String sntName) {
        return Button.of(style,new Gson().toJson(new Data("gttsName", permission, sntName)), label);
    }

    public static class Data extends ButtonData {
        private final String name;

        public Data(String id, Perms permission, String name) {
            super(id, permission);
            this.name = name;
        }

        public static Data from(String componentId) {
            return GSON.fromJson(componentId, Data.class);
        }
    }
}
