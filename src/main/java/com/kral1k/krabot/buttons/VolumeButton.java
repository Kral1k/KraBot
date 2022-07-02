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

public class VolumeButton {
    private static final String id = "volume";

    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register(id, interaction -> {

            Data data = Data.from(interaction.getComponentId());
            MusicPlayer player = interaction.getGuild().getMusicPlayer();

//            Player player = data.synthesize ? callback.getGuild().getSynthesizer().getPlayer() : callback.getGuild().getMusicPlayer();
            EmbedBuilder embedBuilder = new EmbedBuilder();

            int newVolume = data.volume;
            int oldVolume = player.audioPlayer.getVolume();
            if (newVolume != oldVolume) {
                player.audioPlayer.setVolume(newVolume);
                embedBuilder.setTitle(Text.translatable("player.volume.replace", oldVolume, newVolume));
                embedBuilder.setColor(Color.GREEN);
            } else {
                embedBuilder.setTitle(Text.translatable("player.volume.warn", newVolume));
                embedBuilder.setColor(Color.ORANGE);
            }

            interaction.editMessageEmbeds(embedBuilder.build()).queue();
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, int volume) {
        return Button.of(style, new Gson().toJson(new Data(id, permission, volume)), label);
    }

//    public static Button create(String label, ButtonStyle style, Perms permission, int volume, boolean tts) {
//        return Button.of(style, new Gson().toJson(new Data("volume", permission, volume, tts)), label);
//    }

    public static class Data extends ButtonData {
        private final Integer volume;
//        private boolean synthesize;

        public Data(String id, Perms permissions, int volume) {
            super(id, permissions);
            this.volume = volume;
        }

//        public Data(String name, Perms permissions, int volume, boolean synthesize) {
//            super(name, permissions);
//            this.volume = volume;
//            this.synthesize = synthesize;
//        }

        public static Data from(String componentId) {
            return GSON.fromJson(componentId, Data.class);
        }
    }
}
