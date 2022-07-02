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

public class PauseButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("pause", interaction -> {
            MusicPlayer player = interaction.getGuild().getMusicPlayer();
            boolean pause = !player.audioPlayer.isPaused();
            player.audioPlayer.setPaused(pause);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(Text.translatable("player.pause", Text.translatable(pause ? "player.pause.off" : "player.pause.on")));
            embedBuilder.setColor(pause ? Color.ORANGE : Color.GREEN);
            interaction.editMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission) {
        return Button.of(style, new Gson().toJson(new ButtonData("pause", permission)), label);
    }
}
