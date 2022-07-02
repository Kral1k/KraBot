package com.kral1k.krabot.player.tts;

import com.kral1k.krabot.guild.Guild;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TextToSpeechEventAdapter extends AudioEventAdapter {

    private final Guild guild;

    public TextToSpeechEventAdapter(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.guild.getAudioManager().setSendingHandler(this.guild.getMusicPlayer().sendHandler);
    }
}
