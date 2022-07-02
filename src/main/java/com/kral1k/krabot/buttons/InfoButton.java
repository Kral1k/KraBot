package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.Perms;
import com.kral1k.krabot.player.music.MusicPlayer;
import com.kral1k.krabot.utils.Util;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

public class InfoButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("info", interaction -> {
            MusicPlayer player = interaction.getGuild().getMusicPlayer();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            AudioTrack audioTrack = player.audioPlayer.getPlayingTrack();
            if(audioTrack != null){
                embedBuilder.setTitle(Text.translatable("player.info", ""));
                embedBuilder.setDescription("`" + Util.getTimestamp(audioTrack.getInfo().length) + "` " + audioTrack.getInfo().title);
                embedBuilder.setColor(Color.GREEN);
            } else {
                embedBuilder.setTitle(Text.translatable("player.info.null"));
                embedBuilder.setColor(Color.RED);
            }

            interaction.editMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission) {
        return Button.of(style, new Gson().toJson(new ButtonData("info", permission)), label);
    }
}
