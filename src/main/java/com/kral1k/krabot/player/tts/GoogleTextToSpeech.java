package com.kral1k.krabot.player.tts;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.texttospeech.v1beta1.*;
import com.kral1k.krabot.guild.Guild;
import com.kral1k.krabot.utils.GuildDirectory;
import com.sedmelluq.discord.lavaplayer.container.mp3.Mp3AudioTrack;
import com.sedmelluq.discord.lavaplayer.tools.io.NonSeekableInputStream;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class GoogleTextToSpeech {
    private static final AudioConfig audioConfig = AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();
    private final Guild guild;
    private final TextToSpeechPlayer textToSpeechPlayer;
    private final TextToSpeechClient textToSpeechClient;
    private VoiceSelectionParams voiceSelectionParams;

    public GoogleTextToSpeech(Guild guild) throws IOException {
        this.guild = guild;
        Path path = guild.getDirectory(GuildDirectory.ROOT).resolve("GOOGLE_APPLICATION_CREDENTIALS.json");
        if (Files.notExists(path)) throw new FileNotFoundException();
        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(Files.newInputStream(path));
        CredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
        this.textToSpeechClient = TextToSpeechClient.create(settings);
        this.textToSpeechPlayer = new TextToSpeechPlayer(guild);
        this.setName(guild.getData().gTtsName);
    }

    public void setName(String name) {
        if (voiceSelectionParams != null && voiceSelectionParams.getName().equals(name)) return;
        this.voiceSelectionParams = VoiceSelectionParams.newBuilder().setLanguageCode(name.substring(0, 5)).setName(name).build();
        if (guild.getData().gTtsName.equals(name)) return;
        guild.getData().gTtsName = name;
        guild.getData().serialize();
    }

    public TextToSpeechPlayer getPlayer() {
        return textToSpeechPlayer;
    }

    public void sample(String file, Consumer<Boolean> consumer) {
        textToSpeechPlayer.load(file, consumer);
    }

    public void synthesize(String text) {
        SynthesisInput synthesisInput = SynthesisInput.newBuilder().setText(text).build();
        SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(synthesisInput, voiceSelectionParams, audioConfig);

        AudioTrackInfo info = new AudioTrackInfo("tts", "google", -1, "tts", true, "");
        AudioTrack track = new Mp3AudioTrack(info, new NonSeekableInputStream(response.getAudioContent().newInput()));
        textToSpeechPlayer.play(track);
    }


    public static GoogleTextToSpeech create(Guild guild) {
        try {
            return new GoogleTextToSpeech(guild);
        }catch (IOException ex) {
            return null;
        }
    }
}
