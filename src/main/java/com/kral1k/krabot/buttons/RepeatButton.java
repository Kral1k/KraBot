package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.Perms;
import com.kral1k.krabot.player.music.MusicPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

public class RepeatButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("repeat", interaction -> {
            MusicPlayer musicPlayer = interaction.getGuild().getMusicPlayer();
            boolean repeat = !musicPlayer.isRepeating();
            musicPlayer.setRepeating(repeat);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(Text.translatable("player.repeat", Text.translatable(repeat ? "player.repeat.on" : "player.repeat.off")));
            embedBuilder.setColor(repeat ? Color.GREEN : Color.RED);
            interaction.editMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission) {
        return Button.of(style, new Gson().toJson(new ButtonData("repeat", permission)), label);
    }
}
