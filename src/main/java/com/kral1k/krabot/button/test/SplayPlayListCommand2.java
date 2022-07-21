package com.kral1k.krabot.button.test;

import com.kral1k.krabot.api.YouTubeSearch;
import com.kral1k.krabot.api.YouTubeSearchResponse;
import com.kral1k.krabot.button.test.ButtonManager;
import com.kral1k.krabot.buttons.*;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.permission.Perms;
import com.kral1k.krabot.player.music.MusicPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SplayPlayListCommand2 {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("splplay", "splplay").addOption(new OptionData(STRING, "name", "Название трека", true)).predicate(source -> {
            return source.hasPermission(PermissionRole.DJ);
        }).executor(interaction -> {
            String name = interaction.getOption("name").getAsString();
            String key = interaction.getGuild().getProperties().youTubeApiToken;

            interaction.deferReply().queue();
            InteractionHook hook = interaction.getHook();

            YouTubeSearch.newRequest(key, name).setType(YouTubeSearch.Type.PLAYLIST).setRegionCode("UA").sendAsync((statusCode, body) -> {
                if (statusCode != 200) hook.sendMessage(Text.translatable("api.error")).queue();
                else if (body.getItems().isEmpty()) hook.sendMessage(Text.translatable("notFound")).queue();
                else {
                    Collection<Button> playListButtons = new ArrayList<>();
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    for (int i = 0; i < Math.min(5, body.getItems().size()); i++) {
                        YouTubeSearchResponse.Body.Item item = body.getItem(i);
                        String playlistId = item.getId().getPlaylistId();
                        String title = item.getSnippet().getTitle();
                        stringJoiner.add("**" + (i+1) + ")** " + title);
                        playListButtons.add(ButtonManager.newBuilder().predicate(PermissionRole.DJ).execute(event -> {
                            AudioChannel audioChannel = interaction.getMember().getVoiceChannel();
                            if (audioChannel == null) event.replyTranslatable("player.channelNotFound").setEphemeral(true).queue();
                            else {
                                event.deferEdit().queue();
                                event.getGuild().getAudioManager().openAudioConnection(audioChannel);
                                MusicPlayer musicPlayer = event.getGuild().getMusicPlayer();
                                musicPlayer.load("https://www.youtube.com/playlist?list=" + playlistId, new AudioLoadResultHandler() {
                                    private final EmbedBuilder embedBuilder = new EmbedBuilder();
                                    private void send() {
                                        event.getHook().editOriginalEmbeds(embedBuilder.build()).queue();
                                    }
                                    @Override
                                    public void trackLoaded(AudioTrack track) {
                                        embedBuilder.setTitle(Text.translatable("notFound"));
                                        embedBuilder.setColor(Color.RED);
                                        this.send();
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
                                        this.send();
                                    }
                                    @Override
                                    public void noMatches() {
                                        embedBuilder.setTitle(Text.translatable("notFound"));
                                        embedBuilder.setColor(Color.RED);
                                        this.send();
                                    }
                                    @Override
                                    public void loadFailed(FriendlyException exception) {
                                        embedBuilder.setTitle(Text.translatable("player.error"));
                                        embedBuilder.setColor(Color.RED);
                                        this.send();
                                    }
                                });
                            }
                        }).setLabel((i+1) + " ▶").setStyle(ButtonStyle.PRIMARY).build());
                    }
                    ActionRow playListAction = ActionRow.of(playListButtons);
                    ActionRow controlAction = ActionRow.of(
                            PauseButton.create("▶||", ButtonStyle.SUCCESS, new Perms(PermissionRole.DJ)),
                            StopButton.create("▉", ButtonStyle.DANGER, new Perms(PermissionRole.DJ)),
                            SkipButton.create("▶▶|", ButtonStyle.PRIMARY, new Perms(PermissionRole.DJ)),
                            DeleteButton.create(Text.translatable("remove"), ButtonStyle.SECONDARY, new Perms(interaction.getUser().getId()))
                    );
                    hook.sendMessage(stringJoiner.toString()).addActionRows(playListAction, controlAction).delay(30, TimeUnit.MINUTES).flatMap(Message::delete).queue(null, error -> {});
                }
            });
        });
    }
}
