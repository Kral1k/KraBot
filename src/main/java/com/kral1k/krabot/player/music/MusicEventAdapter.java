package com.kral1k.krabot.player.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class MusicEventAdapter extends AudioEventAdapter {

    private final MusicPlayer musicPlayer;
    private AudioTrack lastTrack;

    public MusicEventAdapter(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
    }

    public AudioTrack getLastTrack() {
        return lastTrack;
    }

    @Override
    public void onTrackEnd(AudioPlayer audioPlayer, AudioTrack audioTrack, AudioTrackEndReason endReason) {
        this.lastTrack = audioTrack;
        if (endReason.mayStartNext) {
            if (musicPlayer.isRepeating()) audioPlayer.startTrack(lastTrack.makeClone(), false);
            else musicPlayer.nextTrack();
        }
    }
}
