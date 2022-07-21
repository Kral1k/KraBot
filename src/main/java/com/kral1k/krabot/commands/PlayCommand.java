package com.kral1k.krabot.commands;

import com.kral1k.krabot.api.YouTubeSearch;
import com.kral1k.krabot.command.CommandDispatcher;
import com.kral1k.krabot.command.CommandException;
import com.kral1k.krabot.command.GuildCommandInteraction;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.permission.PermissionRole;
import com.kral1k.krabot.player.music.MusicPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.StringJoiner;
import java.util.function.Consumer;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class PlayCommand {
    public static void register(CommandDispatcher<GuildCommandInteraction> dispatcher) {
        dispatcher.register("play", "play music").addOption(new OptionData(STRING, "url", "Название трека или url", true)).predicate(source -> {
            return source.hasPermission(PermissionRole.DJ);
        }).executor(interaction -> {
            AudioChannel audioChannel = interaction.getMember().getVoiceChannel();
            if(audioChannel == null) throw new CommandException(Text.translatable("player.channelNotFound"));
            interaction.getGuild().getAudioManager().openAudioConnection(audioChannel);

            String name = interaction.getOption("url").getAsString();
            String key = interaction.getGuild().getProperties().youTubeApiToken;

            interaction.deferReply(true).queue();
            InteractionHook hook = interaction.getHook();

            MusicPlayer musicPlayer = interaction.getGuild().getMusicPlayer();

            Consumer<String> audioLoader = url -> musicPlayer.load(url, new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
                    hook.sendMessage(Text.translatable(musicPlayer.isPlayingTrack() ? "player.queue" : "player.play", audioTrack.getInfo().title)).queue();
                    musicPlayer.playOrQueue(audioTrack);
                }

                @Override
                public void playlistLoaded(AudioPlaylist playlist) {
                    StringJoiner stringJoiner = new StringJoiner("\n");
                    stringJoiner.add(Text.translatable("player.playList", playlist.getName(), playlist.getTracks().size()));
                    musicPlayer.queue(playlist.getTracks());
                    if (musicPlayer.getPlayingTrack() == null) {
                        AudioTrack audioTrack = musicPlayer.queue.poll();
                        if (audioTrack != null) {
                            stringJoiner.add(Text.translatable("player.play", audioTrack.getInfo().title));
                            musicPlayer.play(audioTrack);
                        }
                    }
                    hook.sendMessage(stringJoiner.toString()).queue();
                }

                @Override
                public void noMatches() {
                    hook.sendMessage(Text.translatable("notFound")).queue();
                }

                @Override
                public void loadFailed(FriendlyException exception) {
                    hook.sendMessage(Text.translatable("player.error")).queue();
                }
            });

            if (name.startsWith("http://") || name.startsWith("https://") ) audioLoader.accept(name);
            else YouTubeSearch.newRequest(key, name).setType(YouTubeSearch.Type.VIDEO).setCategory(YouTubeSearch.Category.MUSIC).setRegionCode("UA").sendAsync((statusCode, body) -> {
                if (statusCode != 200) hook.sendMessage(Text.translatable("api.error")).queue();
                else if (body.isItemEmpty()) hook.sendMessage(Text.translatable("notFound")).queue();
                else audioLoader.accept("https://www.youtube.com/watch?v=" + body.getItem(0).getId().getVideoId());
            });
        });
    }
}
