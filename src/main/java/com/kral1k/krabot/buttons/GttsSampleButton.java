package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.Perms;
import com.kral1k.krabot.player.tts.GoogleTextToSpeech;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

public class GttsSampleButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("gttsSample", interaction -> {
            AudioChannel audioChannel = interaction.getMember().getVoiceChannel();
            if(audioChannel == null) {
                interaction.reply(Text.translatable("player.channelNotFound")).setEphemeral(true).queue();
                return;
            }
            interaction.getGuild().getAudioManager().openAudioConnection(audioChannel);
            GoogleTextToSpeech gTts = interaction.getGuild().getGTts();
            Data data = Data.from(interaction.getComponentId());

            if (gTts == null) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Error");
                embedBuilder.setColor(Color.RED);
                interaction.editMessageEmbeds(embedBuilder.build()).queue();
                return;
            }

            interaction.deferEdit().queue();

            gTts.sample("./" + data.name + ".mp3", aBoolean -> {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                if (aBoolean) {
                    embedBuilder.setTitle("Воспроизвожу " + data.name);
                    embedBuilder.setColor(Color.GREEN);
                }else {
                    embedBuilder.setTitle("Error");
                    embedBuilder.setColor(Color.RED);
                }
                interaction.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
            });
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, String name) {
        return Button.of(style, new Gson().toJson(new Data("gttsSample", permission, name)), label);
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
