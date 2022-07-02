package com.kral1k.krabot.player.tts;

import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.player.BasePlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.function.Consumer;

public class TextToSpeechPlayer extends BasePlayer {

    public final Guild guild;
    public final TextToSpeechSendHandler sendHandler;
    public final TextToSpeechEventAdapter eventAdapter;


    public TextToSpeechPlayer(Guild guild) {
        this.guild = guild;
        this.sendHandler = new TextToSpeechSendHandler(this.audioPlayer);
        this.eventAdapter = new TextToSpeechEventAdapter(guild);
        this.audioPlayer.addListener(eventAdapter);
        this.audioPlayer.setVolume(100);
    }

    public void play(AudioTrack audioTrack) {
        audioPlayer.stopTrack();
        guild.getAudioManager().setSendingHandler(sendHandler);
        audioPlayer.playTrack(audioTrack);
    }

    public void load(String url, Consumer<Boolean> consumer) {
        this.audioPlayerManager.loadItemOrdered(this, url, new AudioLoadResultHandler() {
            public void trackLoaded(AudioTrack audioTrack) {
                play(audioTrack);
                consumer.accept(true);
            }
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                consumer.accept(false);
            }
            public void noMatches() {
                consumer.accept(false);
            }
            public void loadFailed(FriendlyException e) {
                consumer.accept(false);
            }
        });
    }
}
