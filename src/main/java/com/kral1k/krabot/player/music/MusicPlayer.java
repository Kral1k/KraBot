package com.kral1k.krabot.player.music;

import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.language.Text;
import com.kral1k.krabot.player.BasePlayer;
import com.kral1k.krabot.utils.Util;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;

public class MusicPlayer extends BasePlayer {
    public final MusicSendHandler sendHandler;
    public final MusicEventAdapter eventAdapter;

    public final Queue<AudioTrack> queue = new LinkedList<>();
    private boolean repeating = false;

    public MusicPlayer(Guild guild) {
        this.sendHandler = new MusicSendHandler(this.audioPlayer);
        this.eventAdapter = new MusicEventAdapter(this);
        this.audioPlayer.addListener(this.eventAdapter);
        this.audioPlayer.setVolume(50);
        guild.getAudioManager().setSendingHandler(this.sendHandler);
    }

    @Nullable
    public AudioTrack getPlayingTrack() {
        return audioPlayer.getPlayingTrack();
    }

    public boolean isPlayingTrack() {
        return audioPlayer.getPlayingTrack() != null;
    }

    public void load(String url, AudioLoadResultHandler resultHandler) {
        audioPlayerManager.loadItemOrdered(this, url, resultHandler);
    }

    public void play(AudioTrack audioTrack) {
        audioPlayer.playTrack(audioTrack);
    }

    public void playOrQueue(AudioTrack audioTrack) {
        if (!audioPlayer.startTrack(audioTrack, true)) {
            queue.offer(audioTrack);
        }
    }

    public void queue(AudioTrack audioTrack) {
        queue.offer(audioTrack);
    }

    public void queue(List<AudioTrack> playList) {
        queue.addAll(playList);
    }

    public void skip() {
        this.nextTrack();
    }

    public void stop() {
        queue.clear();
        audioPlayer.stopTrack();
        audioPlayer.setPaused(false);
    }
    public void nextTrack() {
        audioPlayer.playTrack(queue.poll());
    }

    public AudioTrack getLastTrack() {
        return eventAdapter.getLastTrack();
    }

    public boolean isRepeating() {
        return repeating;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    public MessageEmbed listEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        synchronized (queue) {
            if (queue.isEmpty()) {
                embedBuilder.setTitle(Text.translatable("player.list.null"));
                embedBuilder.setColor(Color.RED);
            } else {
                int trackCount = 0;
                long queueLength = 0;
                StringJoiner stringJoiner = new StringJoiner("\n");
                for (AudioTrack track : queue) {
                    queueLength += track.getDuration();
                    if (trackCount < 10) {
                        stringJoiner.add("`" + Util.getTimestamp(track.getDuration()) + "` " + track.getInfo().title);
                        trackCount++;
                    }
                }
                embedBuilder.setTitle(Text.translatable("player.list", queue.size()));
                embedBuilder.setDescription(stringJoiner.toString());
                embedBuilder.setFooter(Text.translatable("player.list.time", Util.getTimestamp(queueLength)));
                embedBuilder.setColor(Color.GREEN);
            }
        }
        return embedBuilder.build();
    }
}
