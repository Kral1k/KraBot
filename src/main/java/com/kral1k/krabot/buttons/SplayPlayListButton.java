package com.kral1k.krabot.buttons;

import com.google.gson.Gson;
import com.kral1k.krabot.button.ButtonData;
import com.kral1k.krabot.button.ButtonDispatcher;
import com.kral1k.krabot.button.GuildButtonInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.Perms;
import com.kral1k.krabot.player.music.MusicPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

public class SplayPlayListButton {
    public static void register(ButtonDispatcher<GuildButtonInteraction> dispatcher) {
        dispatcher.register("splplay", interaction -> {
            AudioChannel audioChannel = interaction.getMember().getVoiceChannel();
            if (audioChannel == null) {
                interaction.reply(Text.translatable("player.channelNotFound")).setEphemeral(true).queue();
            } else {
                interaction.getEvent().deferEdit().queue();
                interaction.getGuild().getAudioManager().openAudioConnection(audioChannel);

                MusicPlayer musicPlayer = interaction.getGuild().getMusicPlayer();
                Data data = Data.from(interaction.getComponentId());

                musicPlayer.load("https://www.youtube.com/playlist?list=" + data.url, new AudioLoadResultHandler() {
                    private final EmbedBuilder embedBuilder = new EmbedBuilder();
                    @Override
                    public void trackLoaded(AudioTrack audioTrack) {

                        if (musicPlayer.isPlayingTrack()){
                            embedBuilder.setTitle(Text.translatable("player.queue", audioTrack.getInfo().title));
                            embedBuilder.setColor(Color.YELLOW);
                        }else{
                            embedBuilder.setTitle(Text.translatable("player.play", audioTrack.getInfo().title));
                            embedBuilder.setColor(Color.GREEN);
                        }
                        interaction.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
                        musicPlayer.playOrQueue(audioTrack);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist playlist) {
                        embedBuilder.setTitle(Text.translatable("player.playList", playlist.getName(), playlist.getTracks().size()));
                        embedBuilder.setColor(Color.GREEN);
                        musicPlayer.queue(playlist.getTracks());
                        if (musicPlayer.getPlayingTrack() == null) {
                            AudioTrack audioTrack = musicPlayer.queue.poll();
                            if (audioTrack != null) {
                                embedBuilder.setDescription(Text.translatable("player.play", audioTrack.getInfo().title));
                                musicPlayer.play(audioTrack);
                            }
                        }
                        interaction.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
                    }

                    @Override
                    public void noMatches() {
                        embedBuilder.setTitle(Text.translatable("notFound"));
                        embedBuilder.setColor(Color.RED);
                        interaction.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
                    }

                    @Override
                    public void loadFailed(FriendlyException e) {
                        embedBuilder.setTitle(Text.translatable("player.error"));
                        embedBuilder.setColor(Color.RED);
                        interaction.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
                    }
                });
            }
        });
    }

    public static Button create(String label, ButtonStyle style, Perms permission, String url) {
        return Button.of(style, new Gson().toJson(new Data("splplay", permission, url)), label);
    }

    public static class Data extends ButtonData {
        private final String url;

        public Data(String id, Perms permission, String url) {
            super(id, permission);
            this.url = url;
        }

        public static Data from(String componentId) {
            return GSON.fromJson(componentId, Data.class);
        }
    }
}
