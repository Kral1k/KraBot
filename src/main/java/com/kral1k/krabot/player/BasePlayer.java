package com.kral1k.krabot.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;

public class BasePlayer {
    public final AudioPlayer audioPlayer;
    protected final AudioPlayerManager audioPlayerManager;

    public BasePlayer() {
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        this.audioPlayerManager.registerSourceManager(new LocalAudioSourceManager());
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        this.audioPlayer = audioPlayerManager.createPlayer();
    }
}
